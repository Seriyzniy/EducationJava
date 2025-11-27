
module userfuncsimpl {
    requires userfuncs;
    
    provides userfuncs.binaryfuncs.BinFuncProvider with
        userfuncsimpl.binaryfuncsimpl.AbsPlusProvider,
        userfuncsimpl.binaryfuncsimpl.AbsMinusProvider;
}