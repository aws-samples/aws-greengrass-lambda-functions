package com.amazonaws.greengrass.cddsensehat.leds.characters;

import javax.inject.Inject;

public class BasicCharacters implements Characters {
    public static final int MAX_CHARACTERS = 256;

    @Inject
    public BasicCharacters() {
    }

    private AbstractCharacter[] characters;

    @Override
    public AbstractCharacter get(char character) {
        if (characters == null) {
            characters = new AbstractCharacter[MAX_CHARACTERS];

            characters['0'] = new Character0();
            characters['1'] = new Character1();
            characters['2'] = new Character2();
            characters['3'] = new Character3();
            characters['4'] = new Character4();
            characters['5'] = new Character5();
            characters['6'] = new Character6();
            characters['7'] = new Character7();
            characters['8'] = new Character8();
            characters['9'] = new Character9();
            characters['v'] = new CharacterLowerV();
            characters['V'] = new CharacterUpperV();
        }

        return characters[character];
    }
}
