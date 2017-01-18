package ru.investflow.mql.parser;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class ParsingContext {
    @NotNull
    public final Stack<IElementType> bracketsStack = new Stack<>();
}
