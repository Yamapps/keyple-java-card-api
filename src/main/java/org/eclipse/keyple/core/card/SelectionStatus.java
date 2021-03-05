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

import org.eclipse.keyple.core.util.json.KeypleGsonParser;

/**
 * This POJO contains the card selection status.
 *
 * @since 2.0
 */
public class SelectionStatus {
  private final AnswerToReset atr;
  private final ApduResponse fci;
  private final boolean hasMatched;

  /**
   * Constructor.
   *
   * <p>Note: ATR and FCI are optional but cannot both be null at the same time.
   *
   * @param atr The power on sequence data (optional).
   * @param fci The answer to select (optional).
   * @param hasMatched A boolean.
   * @throws IllegalStateException if both atr and fci are null at the same time.
   * @since 2.0
   */
  public SelectionStatus(AnswerToReset atr, ApduResponse fci, boolean hasMatched) {

    if (atr == null && fci == null) {
      throw new IllegalStateException("ATR and FCI are both null.");
    }

    this.atr = atr;
    this.fci = fci;
    this.hasMatched = hasMatched;
  }

  /**
   * Gets the power on sequence data.
   *
   * @return null the ATR is not available.
   * @since 2.0
   */
  public AnswerToReset getAtr() {
    return atr;
  }

  /**
   * Gets the {@link ApduResponse} from the card to the <b>Selection Application</b> command.
   *
   * @return null if the FCI is not available.
   * @since 2.0
   */
  public ApduResponse getFci() {
    return fci;
  }

  /**
   * Gives the selection process status.
   *
   * @return true if the card has matched the selection.
   * @since 2.0
   */
  public boolean hasMatched() {
    return hasMatched;
  }

  /**
   * Converts the selection status into a string where the data is encoded in a json format.
   *
   * @return A not empty String
   * @since 2.0
   */
  @Override
  public String toString() {
    return "SELECTION_STATUS = " + KeypleGsonParser.getParser().toJson(this);
  }
}
