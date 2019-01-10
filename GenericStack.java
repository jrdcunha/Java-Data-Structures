/**
 * Lab 4: Generics <br />
 * The {@code GenericStack} class
 */
import java.util.Vector;

public class GenericStack<T> extends Vector<T> {

    public GenericStack() {
        super(0, 1);
    }

    /**
     * Query the top element
     * @return          {@code T} the top element
     */
    public T peek() {
        // TODO: Lab 4 Part 1-1 -- GenericStack, finish the peek method
        if (isEmpty())
            return null;
        else
            return get(size() - 1);
    }

    /**
     * Add a new element as top element
     * @param value     {@code T} the new element
     */
    public void push(T value) {
        // TODO: Lab 4 Part 1-2 -- GenericStack, finish the push method
        add(value);
        return;
    }

    /**
     * Remove the top element
     * @return          {@code T} the removed element
     */
    public T pop() {
        // TODO: Lab 4 Part 1-3 -- GenericStack, finish the pop method
        if (isEmpty())
            return null;
        else
            return remove(size() - 1);
    }

    /**
     * Query the size of the stack
     * @return          {@code int} size of the element
     */
    public int size() {
        // TODO: Lab 4 Part 1-4 -- GenericStack, finish the size method
        int count = 0;
        for (T value : this)
            count++;
        return count;
    }

    /**
     * Check if the stack is empty of not
     * @return          {@code boolean} {@code true} for empty; {@code false} for not
     */
    public boolean isEmpty() {
        // TODO: Lab 4 Part 1-5 -- GenericStack, finish the isEmpty method
        return (size() == 0);
    }

    /**
     * Calculate a postfix expression
     * @param exp       {@code String} the postfix expression
     * @return          {@code Double} the value of the expression
     */
    public static Double calcPostfixExpression(String exp) {
        // TODO: Lab 4 Part 1-6 -- GenericStack, calculate postfix expression
        GenericStack<Double> stack = new GenericStack<Double>();
        String[] symbols = exp.split(" ");
        for (String s : symbols) {
            if (s.matches("[0-9]+(.[0-9]+)?"))
                stack.push(Double.parseDouble(s));
            else if (s.matches("[\\+\\-\\*\\/\\^]"))
                stack.push(calc(stack.pop(), stack.pop(), s));
        }

        return stack.pop();
    }

    private static Double calc(Double a, Double b, String operator) {
        Double result = null;
        switch (operator) {
            case "+":
                result = b + a;
                break;
            case "-":
                result = b - a;
                break;
            case "*":
                result = b * a;
                break;
            case "/":
                result = b / a;
                break;
            case "^":
                result = Math.pow(b, a);
                break;
        }
        
        return result;
    }

    /**
     * Main entry
     * @param args      {@code String[]} Command line arguments
     */
    public static void main(String[] args) {
        String[] expressions = {
                "4 1 +",                    // 1: = 5
                "2 6 -",                    // 2: = -4
                "3 3 *",                    // 3: = 9
                "1 4 /",                    // 4: = 0.25
                "2 3 ^",                    // 5: = 8
                "2 1 + 4 * 8 - 3 ^ 6 -",    // 6: 58
        }; // String[] expressions = { ... };
        for (String s: expressions)
            System.out.println(s + " = " + calcPostfixExpression(s));
    }

}
