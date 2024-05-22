package poly

import scala.annotation.tailrec

final class EmptyListException extends Exception(f"Empty list")

enum MyList[+A]:
  case Nil
  case Cons(x: A, xs: MyList[A])

    def isEmpty: Boolean = this match
        case Nil => true
        case _   => false

    def head: A = this match
        case Nil        => throw EmptyListException()
        case Cons(x, _) => x

    def tail: MyList[A] = this match
        case Nil         => throw EmptyListException()
        case Cons(_, xs) => xs

import MyList.*

//you will need append later
def append[A](l1: MyList[A], l2: MyList[A]): MyList[A] =
    l1 match
        case Nil => l2
        case Cons(x,xs) => Cons(x, append(xs, l2))


def map[A, B](f: A => B)(l: MyList[A]): MyList[B] =
  l match
    case Nil         => Nil
    case Cons(x, xs) => Cons(f(x), map(f)(xs))

def filter[A](f: A => Boolean)(l: MyList[A]): MyList[A] =
    ???

def foldRight[A,B](base: B, f: (A,B) => B)(l: MyList[A]): B = 
    ???
    
def reduceRight[A](f: (A,A) => A)(l: MyList[A]): A =
    ???

def forall[A](p: A => Boolean)(l: MyList[A]): Boolean =
    ???

def exists[A](p: A => Boolean)(l: MyList[A]): Boolean =
    ???

def zip[A,B](l: MyList[A], m: MyList[B]): MyList[(A,B)] = 
    ???

def zipWith[A, B, C](l: MyList[A], m: MyList[B])(op: (A, B) => C): MyList[C] =
    ???

def elementsAsStrings[A](l: MyList[A]): MyList[String] =
    ???

def length[A](l: MyList[A]): Int =
    ???

def takeWhilePositive(l: MyList[Int]): MyList[Int] =
    ???

def last[A](l: MyList[A]): A =
    ???

def capitalizeString(l: MyList[Char]): MyList[Char] =
    ???

def wordCount(l: MyList[Char]): Int =
    ???

def flatMap[A, B](f: A => MyList[B])(l: MyList[A]): MyList[B] =
    ???

def flatten[A](l: MyList[MyList[A]]): MyList[A] = 
    ???

def crossProduct[A, B](l1: MyList[A], l2: MyList[B]): MyList[(A, B)] =
    ???

type NodeId = Int
type DirectedEdge = (NodeId, NodeId)
type DirectedGraph = MyList[DirectedEdge]
type Triangle = (NodeId, NodeId, NodeId)

def triangles(edges: DirectedGraph): MyList[Triangle] =
    ???

def sum0(l: MyList[Int]): Int = 
    l match
        case Nil         => 0
        case Cons(x, xs) => x + sum0(xs)
        
def sum1(l: MyList[Int]): Int ={
    //@tailrec // Uncomment this line.
    def sum(l: MyList[Int], acc: Int): Int =
        ???
    sum(l, 0)
}

//@tailrec // Uncomment this line.
def foldLeft[A, B](base: B, f: (B, A) => B)(l: MyList[A]): B =
    ???

def sum0Fold(l: MyList[Int]): Int =
    ???

def sum1Fold(l: MyList[Int]): Int =
    ???

def reverseAppend[A](l1: MyList[A], l2: MyList[A]): MyList[A] =
    ???

val countEven: MyList[Int] => Int =
    ???

val totalLength: MyList[String] => Int =
    ???

val curriedZipWith = 
    ???
