package com.vhdlparser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private static SymbolTable instance = null;

    private Map<String, Symbol> symbols = new HashMap<>();

    private SymbolTable() { }

    @NotNull
    public static SymbolTable get()
    {
        if (instance == null) {
            instance = new SymbolTable();
        }

        return instance;
    }

    @Nullable
    public Symbol lookUp(@Nullable String id) {
        if (id == null)
            return null;

        return symbols.get(id);
    }

    public boolean put(@Nullable String id, @Nullable Symbol symbol) {
        if (id == null || symbol == null)
            return false;

        symbols.put(id, symbol);

        return true;
    }

    public boolean putSignalVector(@Nullable String id, @Nullable Integer rangeLow, @Nullable Integer rangeMax) {
        if (id == null || rangeLow == null || rangeMax == null)
            return false;

        return put(id, new Symbol(id, Symbol.Type.SIGNAL, Symbol.Subtype.VECTOR, rangeLow, rangeMax));
    }

    public boolean putSignalScalar(@Nullable String id) {
        if (id == null)
            return false;

        return put(id, new Symbol(id, Symbol.Type.SIGNAL, Symbol.Subtype.SCALAR));
    }

    public boolean putTypeArray(@Nullable String id, @Nullable List<Integer> ranges) {
        if (id == null || ranges == null)
            return false;

        return put(id, new Symbol(id, Symbol.Type.TYPE, Symbol.Subtype.ARRAY, ranges));
    }

    public boolean putSignalCustom(@Nullable String id, @Nullable String customTypeId) {
        if (id == null || customTypeId == null)
            return false;

        return put(id, new Symbol(id, Symbol.Type.SIGNAL, Symbol.Subtype.CUSTOM, customTypeId));
    }
}
