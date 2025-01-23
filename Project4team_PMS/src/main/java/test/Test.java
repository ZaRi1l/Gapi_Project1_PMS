package test;

import java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		Task task = new Task();
		Dashborad db = new Dashborad();
		task.createTask();
	}
}

class User{
	Task task = new Task();
	Dashborad db = new Dashborad();
	
	
}

class Dashborad{
	
	ArrayList<Task> tk = new ArrayList<>();
	
	
	public void addTask() {
		tk.add(null);
	}
}

class Task{
	
	public void createTask() {
		Dashborad db1 = new Dashborad();
		db1.addTask();
		
		
	}
}
