/* **************************************************************************************
 * Copyright (c) 2018 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.eclipse.keyple.core.card;

import java.util.Set;
import org.eclipse.keyple.core.util.Assert;
import org.eclipse.keyple.core.util.json.JsonUtil;

/**
 * This POJO contains a set of data related to an ISO-7816 APDU command.
 *
 * <ul>
 *   <li>A byte array containing the raw APDU data.
 *   <li>A flag indicating if the APDU is of type 4 (ingoing and outgoing data).
 *   <li>An optional set of integers corresponding to valid status codes in response to this APDU.
 * </ul>
 *
 * Attaching an optional name to the request facilitates the enhancement of the application logs
 * using the toString method.
 *
 * @since 2.0
 */
public final class ApduRequest {

  private final byte[] bytes;
  private final boolean isCase4;
  private Set<Integer> successfulStatusCodes;
  private String name;

  /**
   * Builds an APDU request from its elements as defined by the ISO 7816 standard.
   *
   * <p>The ISO7816 case for data in a command-response pair is determined from the provided
   * arguments:
   *
   * <ul>
   *   <li><code>dataIn &nbsp;= null, le &nbsp;= null</code>&nbsp;&nbsp;&rarr;&nbsp;&nbsp;case 1: no
   *       command data, no response data expected.
   *   <li><code>dataIn &nbsp;= null, le != null</code>&nbsp;&nbsp;&rarr;&nbsp;&nbsp;case 2: no
   *       command data, expected response data.
   *   <li><code>dataIn != null, le &nbsp;= null</code>&nbsp;&nbsp;&rarr;&nbsp;&nbsp;case 3: command
   *       data, no response data expected.
   *   <li><code>dataIn != null, le &nbsp;= 0&nbsp;&nbsp;&nbsp;</code>
   *       &nbsp;&nbsp;&rarr;&nbsp;&nbsp;case 4: command data, expected response data.
   * </ul>
   *
   * Only the indication for case 4 is retained in the end.<br>
   * In this case (incoming and outgoing data for the card), Le is set to 0, letting the lower layer
   * (see API plugin) take care of recovering the exact length of the outgoing data.
   *
   * <p>If dataIn is not null and Le &gt; 0 an IllegalArgumentException is thrown.
   *
   * @param cla The class byte.
   * @param instruction The instruction byte.
   * @param p1 The parameter 1.
   * @param p2 The parameter 2.
   * @param dataIn The data field of the command.
   * @param le The maximum number of bytes expected in the data field of the response to the
   *     command.
   * @throws IllegalArgumentException in case of inconsistencies in the input data.
   * @since 2.0
   */
  public ApduRequest(byte cla, byte instruction, byte p1, byte p2, byte[] dataIn, Byte le) {

    /* consistency check */
    if (dataIn != null && le != null && le != 0) {
      throw new IllegalArgumentException(
          "Le must be equal to 0 when not null and ingoing data are present.");
    }

    /* Buffer allocation */
    bytes = allocateBuffer(dataIn, le);

    /* Build APDU buffer from provided arguments */
    bytes[0] = cla;
    bytes[1] = instruction;
    bytes[2] = p1;
    bytes[3] = p2;

    /* ISO7618 case determination and Le management */
    if (dataIn != null) {
      /* append Lc and ingoing data */
      bytes[4] = (byte) dataIn.length;
      System.arraycopy(dataIn, 0, bytes, 5, dataIn.length);
      if (le != null) {
        /*
         * case4: ingoing and outgoing data, Le is always set to 0 (see Calypso Reader
         * Recommendations - T84)
         */
        isCase4 = true;
        bytes[bytes.length - 1] = 0;
      } else {
        /* case3: ingoing data only, no Le */
        isCase4 = false;
      }
    } else {
      if (le != null) {
        /* case2: outgoing data only */
        bytes[4] = le;
      } else {
        /* case1: no ingoing, no outgoing data, P3/Le = 0 */
        bytes[4] = (byte) 0x00;
      }
      isCase4 = false;
    }
  }

  /**
   * Builds an APDU request from a raw byte buffer and the indication of case 4.
   *
   * @param bytes The bytes of the APDU's body.
   * @param isCase4 true if the APDU is case 4, false if not.
   * @throws IllegalArgumentException if the provided buffer is empty or shorter than 5 bytes
   * @since 2.0
   */
  public ApduRequest(byte[] bytes, boolean isCase4) {
    Assert.getInstance().notNull(bytes, "bytes").greaterOrEqual(bytes.length, 5, "bytes.length");
    this.bytes = bytes;
    this.isCase4 = isCase4;
  }

  /**
   * (private)<br>
   * Returns a byte array having the expected length according the APDU construction rules.
   *
   * @param data Data array (could be null).
   * @param le Expected outgoing length (could be null).
   * @return A new byte array.
   */
  private byte[] allocateBuffer(byte[] data, Byte le) {
    int length = 4; // header
    if (data == null && le == null) {
      // case 1: 5-byte apdu, le=0
      length += 1; // Le
    } else {
      if (data != null) {
        length += data.length + 1; // Lc + data
      }
      if (le != null) {
        length += 1; // Le
      }
    }
    return new byte[length];
  }

  /**
   * Sets a list of status codes that must be considered successful for the APDU.
   *
   * @param successfulStatusCodes A not empty Set of Integer.
   * @return the object instance.
   * @since 2.0
   */
  public ApduRequest setSuccessfulStatusCodes(Set<Integer> successfulStatusCodes) {
    this.successfulStatusCodes = successfulStatusCodes;
    return this;
  }

  /**
   * Gets the list of status codes that must be considered successful for the APDU.
   *
   * @return null if no successful code has been set.
   * @since 2.0
   */
  public Set<Integer> getSuccessfulStatusCodes() {
    return successfulStatusCodes;
  }

  /**
   * Indicates if the APDU is of type case 4.
   *
   * @return True if the APDU is of type case 4, false if not.
   * @since 2.0
   */
  public boolean isCase4() {
    return isCase4;
  }

  /**
   * Names the APDU request.
   *
   * <p>This string is dedicated to improve the readability of logs and should therefore only be
   * invoked conditionally (e.g. when log level &gt;= debug).
   *
   * @param name The request name (free text).
   * @return The object instance.
   * @since 2.0
   */
  public ApduRequest setName(final String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets the name of this APDU request.
   *
   * @return null if no name has been defined.
   * @since 2.0
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the APDU bytes to be sent to the card.
   *
   * @return A not null array.
   * @since 2.0
   */
  public byte[] getBytes() {
    return this.bytes;
  }

  /**
   * Converts the APDU request into a string where the data is encoded in a json format.
   *
   * @return A not empty String
   * @since 2.0
   */
  @Override
  public String toString() {
    return "APDU_REQUEST = " + JsonUtil.toJson(this);
  }
}
