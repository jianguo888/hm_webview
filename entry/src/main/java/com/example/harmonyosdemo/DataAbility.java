package com.example.harmonyosdemo;

import java.io.FileNotFoundException;
import java.io.IOException;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.global.resource.RawFileDescriptor;
import ohos.utils.net.Uri;

public class DataAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
    }

    @Override
    public RawFileDescriptor openRawFile(Uri uri, String mode) throws FileNotFoundException {
        if (uri == null) {
            return super.openRawFile(uri, mode);
        }
        String path = uri.getEncodedPath();
        final int splitIndex = path.indexOf('/', 1);
        final String providerName = Uri.decode(path.substring(1, splitIndex));
        String rawFilePath = Uri.decode(path.substring(splitIndex + 1));
        RawFileDescriptor rawFileDescriptor = null;
        try {
            rawFileDescriptor = getResourceManager().getRawFileEntry(rawFilePath).openRawFileDescriptor();
        } catch (IOException e) {
            // 处理异常
        }
        return rawFileDescriptor;
    }
}
