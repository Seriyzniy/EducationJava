package userfuncsimpl.binaryfuncsimpl;

import userfuncs.binaryfuncs.BinFuncProvider;
import userfuncs.binaryfuncs.BinaryFunc;

public class AbsMinusProvider implements BinFuncProvider {
    // Предоставить объект AbsMinus
    public BinaryFunc get() {
        return new AbsMinus();
    }
}
