@FunctionalInterface
interface AA{
	void add(int a,int b);
}
public class LambdaExpressionEx2 {

	public static void main(String[] args) {
		//Lambda Expression
		AA a=(int x, int y)->{
			int r=x+y;
			System.out.println("Sum: "+r);
		};
		a.add(8,9);
		
		AA a1=(x,y)->{
			int r=x+y;
			System.out.println("Sum: "+r);
		};
		a1.add(8,9);
	}

}
