package net.boster.chat.common.chat.settings;

import org.jetbrains.annotations.Nullable;

public interface Settings {

    @Nullable Setting getDeniedWordsSetting();
    @Nullable Setting getDeniedContentsSetting();
    @Nullable Setting getDenyAddressSetting();
    @Nullable Setting getSimilarityCheckSetting();
    @Nullable Setting getMessageCapitalize();
    @Nullable Setting getMessageColorize();
    @Nullable Setting getMessageDotInsertion();

    Settings clone();
}
