@FunctionalInterface
interface A{
	void add();
}
public class LambdaExpressionEx1 {

	public static void main(String[] args) {
		
//		A a=new A() {
//			public void add() {
//				int x=10,y=20,r=x+y;
//				System.out.println("Sum: "+r);
//			}
//		};
		
		//Lambda Expression
		A a=()->{
			int x=10,y=20,r=x+y;
			System.out.println("Sum: "+r);
		};
		a.add();
	}

}
