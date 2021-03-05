/* **************************************************************************************
 * Copyright (c) 2020 Calypso Networks Association https://www.calypsonet-asso.org/
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

import org.eclipse.keyple.core.common.KeypleCardSelectionResponse;
import org.eclipse.keyple.core.util.Assert;
import org.eclipse.keyple.core.util.json.KeypleGsonParser;

/**
 * This POJO contains the data from a card obtained in response to a card selection request.
 *
 * <p>These data are the selection status ({@link SelectionStatus}) and the responses, if any, to
 * the additional APDUs sent to the card ({@link CardResponse}).
 *
 * @see CardSelectionRequest
 * @since 2.0
 */
public final class CardSelectionResponse implements KeypleCardSelectionResponse {

  private final SelectionStatus selectionStatus;
  private final CardResponse cardResponse;

  /**
   * Builds a card selection response from the {@link SelectionStatus} and a {@link CardResponse}
   * (list of {@link ApduResponse}).
   *
   * @param selectionStatus The selection status.
   * @param cardResponse The card response.
   * @throws IllegalArgumentException if one of the argument is null.
   * @since 2.0
   */
  public CardSelectionResponse(SelectionStatus selectionStatus, CardResponse cardResponse) {

    Assert.getInstance()
        .notNull(selectionStatus, "selectionStatus")
        .notNull(cardResponse, "cardResponse");
    this.selectionStatus = selectionStatus;
    this.cardResponse = cardResponse;
  }

  /**
   * Gets the selection status.
   *
   * @return A not null reference.
   * @since 2.0
   */
  public SelectionStatus getSelectionStatus() {
    return this.selectionStatus;
  }

  /**
   * Gets the card response.
   *
   * @return A not null reference.
   * @since 2.0
   */
  public CardResponse getCardResponse() {
    return cardResponse;
  }

  /**
   * Converts the card selection response into a string where the data is encoded in a json format.
   *
   * @return A not empty String
   * @since 2.0
   */
  @Override
  public String toString() {
    return "CARD_SELECTION_RESPONSE = " + KeypleGsonParser.getParser().toJson(this);
  }
}
