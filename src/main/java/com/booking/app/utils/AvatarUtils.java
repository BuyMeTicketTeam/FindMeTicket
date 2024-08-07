package com.booking.app.utils;

import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.GitHubAvatar;
import com.talanlabs.avatargenerator.layers.backgrounds.ColorPaintBackgroundLayer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class AvatarUtils {

    public static byte[] createRandomAvatarAsBytes() {
        Avatar avatar = GitHubAvatar.newAvatarBuilder().layers(new ColorPaintBackgroundLayer(Color.WHITE)).build();
        return avatar.createAsPngBytes(new Random().nextLong());
    }

}
