public class Tutor{
	private Computer[] computers;
	
	public Tutor(int numberComputers) throws IllegalArgumentException{
		if( numberComputers <= 0 ){
			throw new IllegalArgumentException();
		}
		computers = new Computer[numberComputers];
		for(int i = 0; i < numberComputers; i++){
			computers[i] = new Computer();
		}
	}
	
	public int getNumberComputers(){
		return computers.length;
	}
	
	public void studentUsesComputer(int index) throws IllegalArgumentException{
		if( index < 1 || index > computers.length )
			throw new IllegalArgumentException();
		computers[index-1].studentUsesComputer();
	}
	
	public void studentLeavesComputer(int index) throws IllegalArgumentException{
		if( index < 1 || index > computers.length )
			throw new IllegalArgumentException();
		computers[index-1].studentLeavesComputer();
	}
	
	public void graduateUsesComputer(int index) throws IllegalArgumentException{
		if( index < 1 || index > computers.length )
			throw new IllegalArgumentException();
		computers[index-1].graduateUsesComputer();
	}
	
	public void graduateLeavesComputer(int index) throws IllegalArgumentException{
		if( index < 1 || index > computers.length )
			throw new IllegalArgumentException();
		computers[index-1].graduateLeavesComputer();
	}
	
	public void useAllComputers(){
		for(int i = 0; i < computers.length; i++){
			computers[i].teacherUsesComputer();
		}
	}
	
	public void leaveAllComputers(){
		for(int i = 0; i < computers.length; i++){
			computers[i].teacherLeavesComputer();
		}
	}
}
