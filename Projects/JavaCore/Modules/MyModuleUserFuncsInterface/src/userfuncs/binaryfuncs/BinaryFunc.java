package userfuncs.binaryfuncs;

public interface BinaryFunc {
	// Получить имя функции
    public String getName();
    
    // Это функция, подлежащая выполнению. Она будет
    // предоставляться конкретными реализациями
    public int func(int a, int b);
}
