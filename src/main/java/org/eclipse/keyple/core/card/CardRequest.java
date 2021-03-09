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

import java.util.List;
import org.eclipse.keyple.core.util.json.JsonUtil;

/**
 * This POJO contains an ordered list of {@link ApduRequest} and the associated status code check
 * policy.
 *
 * @see CardResponse
 * @since 2.0
 */
public final class CardRequest {

  private final List<ApduRequest> apduRequests;
  private final boolean isStatusCodesVerificationEnabled;

  /**
   * Builds a card request with a list of {@link ApduRequest } and the flag indicating the expected
   * response checking behavior.
   *
   * <p>When the status code verification is enabled, the transmission of the APDUs must be
   * interrupted as soon as the status code of a response is unexpected.
   *
   * @param apduRequests A not empty list.
   * @param isStatusCodesVerificationEnabled true or false.
   * @since 2.0
   */
  public CardRequest(List<ApduRequest> apduRequests, boolean isStatusCodesVerificationEnabled) {
    this.apduRequests = apduRequests;
    this.isStatusCodesVerificationEnabled = isStatusCodesVerificationEnabled;
  }

  /**
   * Gets the list of {@link ApduRequest}.
   *
   * @return A not empty list.
   * @since 2.0
   */
  public List<ApduRequest> getApduRequests() {
    return apduRequests;
  }

  /**
   * Gets the status code verification policy.
   *
   * @return true if the status code verification is enabled, false if not.
   * @since 2.0
   */
  boolean isStatusCodesVerificationEnabled() {
    return isStatusCodesVerificationEnabled;
  }

  /**
   * Converts the card request into a string where the data is encoded in a json format.
   *
   * @return A not empty String
   * @since 2.0
   */
  @Override
  public String toString() {
    return "CARD_REQUEST = " + JsonUtil.toJson(this);
  }
}
