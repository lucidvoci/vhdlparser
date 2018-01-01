package com.vhdlparser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public Symbol(@NotNull String name, @NotNull Symbol.Type type, @NotNull Symbol.Subtype subtype, @Nullable Integer rangeLow, @Nullable Integer rangeMax) {
        this.name = name;
        this.type = type;
        this.subtype = subtype;
        this.rangeLow = rangeLow;
        this.rangeMax = rangeMax;
    }

    public Symbol(@NotNull String name, @NotNull Symbol.Type type, @NotNull Symbol.Subtype subtype) {
        this(name, type, subtype, null, null);
    }


    public static enum Type {
        SIGNAL,
        VARIABLE;
    }

    public static enum Subtype {
        SCALAR,
        VECTOR;
    }
}
