@FunctionalInterface
interface Square{
	int sqr(int a);
}
public class LambdaExpressionEx4 {

	public static void main(String[] args) {
		//All allowed
		
//		Square a=(x)->{
//			int r=x*x;
//			return r;
//		};
		
//		Square a=x->{
//			int r=x*x;
//			return r;
//		};
		
//		Square a=x->{
//			return x*x;
//		};
		
		Square a=x -> x*x;
		
		int s=a.sqr(5);
		System.out.println("Square: "+s);
	}

}
