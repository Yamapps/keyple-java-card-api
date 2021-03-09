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

import java.util.Arrays;
import org.eclipse.keyple.core.util.Assert;
import org.eclipse.keyple.core.util.json.JsonUtil;

/**
 * This POJO contains a set of data related to an ISO-7816 APDU response.
 *
 * @since 2.0
 */
public final class ApduResponse {

  private final byte[] bytes;
  private final int statusCode;

  /**
   * Builds an APDU response from an array of bytes from the card, computes the status code.
   *
   * @param bytes A byte array
   * @throws IllegalArgumentException if the provided byte array is null or less than 2 bytes long.
   * @since 2.0
   */
  public ApduResponse(byte[] bytes) {

    Assert.getInstance().notNull(bytes, "bytes").greaterOrEqual(bytes.length, 2, "bytes.length");
    this.bytes = bytes;
    statusCode =
        ((bytes[bytes.length - 2] & 0x000000FF) << 8) + (bytes[bytes.length - 1] & 0x000000FF);
  }

  /**
   * Gets the status code SW1SW2 of the APDU.
   *
   * @return A int.
   * @since 2.0
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * Gets the raw data received from the card (including SW1SW2).
   *
   * @return A not null byte array.
   * @since 2.0
   */
  public byte[] getBytes() {
    return this.bytes;
  }

  /**
   * Gets the data part received from the card response (excluding SW1SW2).
   *
   * @return A not null byte array.
   * @since 2.0
   */
  public byte[] getDataOut() {
    return Arrays.copyOfRange(this.bytes, 0, this.bytes.length - 2);
  }

  /**
   * Converts the APDU response into a string where the data is encoded in a json format.
   *
   * @return A not empty String
   * @since 2.0
   */
  @Override
  public String toString() {
    return "APDU_RESPONSE = " + JsonUtil.toJson(this);
  }
}
