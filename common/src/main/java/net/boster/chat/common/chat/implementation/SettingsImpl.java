package net.boster.chat.common.chat.implementation;

import net.boster.chat.common.chat.settings.*;
import net.boster.chat.common.chat.settings.formatter.CapitalizeSetting;
import net.boster.chat.common.chat.settings.formatter.ColorizeSetting;
import net.boster.chat.common.chat.settings.formatter.DotInsertionSetting;
import net.boster.chat.common.config.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettingsImpl implements Settings, Cloneable {

    private DeniedWordsSetting w;
    private DeniedContentsSetting c;
    private DenyAddressSetting a;
    private SimilarityCheckSetting s;
    private ColorizeSetting cs;
    private CapitalizeSetting ccs;
    private DotInsertionSetting ds;

    public SettingsImpl(@NotNull ConfigurationSection section) {
        ConfigurationSection cw = section.getSection("DeniedWords");
        if(cw != null) {
            this.w = new DeniedWordsSetting(cw);
        }
        ConfigurationSection cc = section.getSection("DeniedContents");
        if(cc != null) {
            this.c = new DeniedContentsSetting(cc);
        }
        ConfigurationSection ca = section.getSection("DenyAddress");
        if(ca != null) {
            this.a = new DenyAddressSetting(ca);
        }
        ConfigurationSection cs = section.getSection("CheckSimilarity");
        if(cs != null) {
            this.s = new SimilarityCheckSetting(cs);
        }
        ConfigurationSection csc = section.getSection("ColorizeMessage");
        if(csc != null) {
            this.cs = new ColorizeSetting(csc);
        }
        ConfigurationSection ccc = section.getSection("CapitalizeMessage");
        if(ccc != null) {
            this.ccs = new CapitalizeSetting(ccc);
        }
        ConfigurationSection dc = section.getSection("DotInsertion");
        if(dc != null) {
            this.ds = new DotInsertionSetting(dc);
        }
    }

    @Override
    public @Nullable DeniedWordsSetting getDeniedWordsSetting() {
        return w;
    }

    @Override
    public @Nullable DeniedContentsSetting getDeniedContentsSetting() {
        return c;
    }

    @Override
    public @Nullable DenyAddressSetting getDenyAddressSetting() {
        return a;
    }

    @Override
    public @Nullable SimilarityCheckSetting getSimilarityCheckSetting() {
        return s;
    }

    @Override
    public @Nullable Setting getMessageCapitalize() {
        return ccs;
    }

    @Override
    public @Nullable Setting getMessageColorize() {
        return cs;
    }

    @Override
    public @Nullable Setting getMessageDotInsertion() {
        return ds;
    }

    @Override
    public Settings clone() {
        try {
            return (Settings) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("It may never happen");
        }
    }
}
