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

/**
 * Reader able to transmit card requests and having control over the physical channel.
 *
 * <p>You can use this API by casting any reader object the Keyple Service API into a ProxyReader.
 *
 * @since 2.0
 */
public interface ProxyReader {

  /**
   * Transmits a {@link CardRequest}, applies the provided {@link ChannelControl} policy and returns
   * a {@link CardResponse}.
   *
   * <p>The APDUs ({@link ApduRequest}) contained in the {@link CardRequest} are sent to the card
   * their responses ({@link ApduResponse}) are added to a new list.
   *
   * <p><b>Note:</b> in case of a communication error when sending an APDU, an {@link
   * AbstractApduException} exception is thrown. Any responses from previous APDU commands are
   * attached to this exception.<br>
   * This allows the calling application to be tolerant to card tearing and to retrieve the partial
   * response to the {@link CardRequest}.
   *
   * @param cardRequest The card request.
   * @param channelControl The channel control policy to apply.
   * @return A not null reference.
   * @throws ReaderCommunicationException If the communication with the reader has failed.
   * @throws CardCommunicationException If the communication with the card has failed.
   * @throws UnexpectedStatusCodeException If any of the APDUs returned an unexpected status code
   *     and the card request specified the need to check them.
   * @since 2.0
   */
  CardResponse transmitCardRequest(CardRequest cardRequest, ChannelControl channelControl)
      throws ReaderCommunicationException, CardCommunicationException,
          UnexpectedStatusCodeException;

  /**
   * Releases the communication channel previously established with the card.
   *
   * @throws ReaderCommunicationException If the communication with the reader has failed.
   * @since 2.0
   */
  void releaseChannel() throws ReaderCommunicationException;
}
