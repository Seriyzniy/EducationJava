package userfuncsimpl.binaryfuncsimpl;

import userfuncs.binaryfuncs.BinFuncProvider;
import userfuncs.binaryfuncs.BinaryFunc;

public class AbsPlusProvider implements BinFuncProvider {
	// Предоставить объект AbsPlus
    public BinaryFunc get() {
        return new AbsPlus();
    }
}
