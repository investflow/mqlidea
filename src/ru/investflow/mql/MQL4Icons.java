package ru.investflow.mql;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.LayeredIcon;

import javax.swing.Icon;

public interface MQL4Icons {
    // MQL4 files
    Icon File = IconLoader.getIcon("/icons/mql4.png");

    // Classes and structs
    Icon Class = AllIcons.Nodes.Class;
    Icon Struct = IconLoader.getIcon("/icons/struct.png");
    Icon Interface = AllIcons.Nodes.Interface;

    // Functions and methods
    Icon FunctionDeclaration = new LayeredIcon(AllIcons.Nodes.Function, AllIcons.Nodes.Symlink);
    Icon FunctionDefinition = AllIcons.Nodes.Function;
    Icon MethodDeclaration = new LayeredIcon(AllIcons.Nodes.Method, AllIcons.Nodes.Symlink);
    Icon MethodDefinition = AllIcons.Nodes.Method;


}
