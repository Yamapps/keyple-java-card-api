/* **************************************************************************************
 * Copyright (c) 2021 Calypso Networks Association https://www.calypsonet-asso.org/
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
/**
 * Exception carrying response data received from the card up to the time of a card communication
 * failure.
 *
 * @since 2.0
 */
public class CardCommunicationException extends AbstractCommunicationException {

  /**
   * Builds a new exception embedding card response data.
   *
   * @param cardResponse The card responses received so far.
   * @param message Message to identify the exception context.
   * @since 2.0
   */
  public CardCommunicationException(CardResponse cardResponse, String message) {
    super(cardResponse, message);
  }

  /**
   * Builds a new exception embedding card response data with the originating exception.
   *
   * @param cardResponse The card responses received so far.
   * @param message Message to identify the exception context.
   * @param cause The cause
   * @since 2.0
   */
  public CardCommunicationException(CardResponse cardResponse, String message, Throwable cause) {
    super(cardResponse, message, cause);
  }
}
