package userfuncsimpl.binaryfuncsimpl;

import userfuncs.binaryfuncs.BinaryFunc;

public class AbsMinus implements BinaryFunc {
	// Возвратить имя этой функции
    public String getName() {
        return "absMinus";
    }
    
    // Реализовать функцию AbsMinus
    public int func(int a, int b) {
        return Math.abs(a) - Math.abs(b);
    }
}
