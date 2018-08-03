
// Copyright 2014 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This file is autogenerated by:
//     mojo/public/tools/bindings/mojom_bindings_generator.py
// For:
//     media/mojo/interfaces/content_decryption_module.mojom
//

package org.chromium.media.mojom;

import org.chromium.base.annotations.SuppressFBWarnings;
import org.chromium.mojo.bindings.DeserializationException;


public interface ContentDecryptionModuleClient extends org.chromium.mojo.bindings.Interface {



    public interface Proxy extends ContentDecryptionModuleClient, org.chromium.mojo.bindings.Interface.Proxy {
    }

    Manager<ContentDecryptionModuleClient, ContentDecryptionModuleClient.Proxy> MANAGER = ContentDecryptionModuleClient_Internal.MANAGER;


    void onSessionMessage(
String sessionId, int messageType, byte[] message);



    void onSessionClosed(
String sessionId);



    void onSessionKeysChange(
String sessionId, boolean hasAdditionalUsableKey, CdmKeyInformation[] keysInfo);



    void onSessionExpirationUpdate(
String sessionId, double newExpiryTimeSec);


}