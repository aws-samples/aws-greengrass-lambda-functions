package com.amazonaws.greengrass.cdddmi.dmi.interfaces;

import java.util.Map;
import java.util.Optional;

public interface DmiFetcher {
    Optional<Map<String, Object>> fetch();
}
