package poly
import MyList.*

final class NotEnoughOperandsInStackException extends Exception()
final class DividedByZeroException extends Exception()

type Stack = MyList[Int]

/*How do you implement the insruction type?
**this is commented such that the rest of the project can be compiled if this exercise is not solved yet
**You may uncomment it when you want to solve this part.

type Program = MyList[Instruction]

def interpreteInst(stack: Stack, inst: Instruction): Stack =
    ???


def interpreteProg(stack: Stack, program: Program): Stack =
    foldLeft(stack, interpreteInst)(program)
*/