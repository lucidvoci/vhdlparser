package com.vhdlparser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class Symbol {
    @NotNull
    private String name;
    @NotNull
    private Symbol.Type type;
    @NotNull
    private Symbol.Subtype subtype;
    @Nullable
    private Integer rangeLow;
    @Nullable
    private Integer rangeMax;
    @Nullable
    private List<Integer> ranges;
    @Nullable
    private String customTypeId;

    @NotNull
    public final String getName() {
        return this.name;
    }
    public final void setName(@NotNull String var1) {
        this.name = var1;
    }

    @NotNull
    public final Symbol.Type getType() {
        return this.type;
    }
    public final void setType(@NotNull Symbol.Type var1) {
        this.type = var1;
    }

    @NotNull
    public final Symbol.Subtype getSubtype() {
        return this.subtype;
    }
    public final void setSubtype(@NotNull Symbol.Subtype var1) {
        this.subtype = var1;
    }

    @Nullable
    public final Integer getRangeLow() {
        return this.rangeLow;
    }
    public final void setRangeLow(@Nullable Integer var1) {
        this.rangeLow = var1;
    }

    @Nullable
    public final Integer getRangeMax() {
        return this.rangeMax;
    }
    public final void setRangeMax(@Nullable Integer var1) {
        this.rangeMax = var1;
    }

    @Nullable
    public final List<Integer> getRanges() {
        return this.ranges;
    }
    public final void setRanges(@Nullable List<Integer> var1) {
        this.ranges = var1;
    }

    @Nullable
    public final String getCustomTypeId() {
        return this.customTypeId;
    }
    public final void setCustomTypeId(@Nullable String var1) {
        this.customTypeId = var1;
    }

    private Symbol(@NotNull String name, @NotNull Symbol.Type type, @NotNull Symbol.Subtype subtype,
                   @Nullable Integer rangeLow, @Nullable Integer rangeMax, @Nullable List<Integer> ranges,
                   @Nullable String customTypeId) {
        this.name = name;
        this.type = type;
        this.subtype = subtype;
        this.rangeLow = rangeLow;
        this.rangeMax = rangeMax;
        this.ranges = ranges;
        this.customTypeId = customTypeId;
    }

    public Symbol(@NotNull String name, @NotNull Symbol.Type type, @NotNull Symbol.Subtype subtype) {
        this(name, type, subtype, null, null, null, null);
    }

    public Symbol(@NotNull String name, @NotNull Symbol.Type type, @NotNull Symbol.Subtype subtype, @NotNull Integer rangeLow, @NotNull Integer rangeMax) {
        this(name, type, subtype, rangeLow, rangeMax, null, null);
    }

    public Symbol(@NotNull String name, @NotNull Symbol.Type type, @NotNull Symbol.Subtype subtype, @NotNull List<Integer> ranges) {
        this(name, type, subtype, null, null, ranges, null);
    }

    public Symbol(@NotNull String name, @NotNull Symbol.Type type, @NotNull Symbol.Subtype subtype, @NotNull String customTypeId) {
        this(name, type, subtype, null, null, null, customTypeId);
    }

    public enum Type {
        SIGNAL,
        VARIABLE,
        TYPE
    }

    public enum Subtype {
        SCALAR,
        VECTOR,
        ARRAY,
        CUSTOM
    }
}
