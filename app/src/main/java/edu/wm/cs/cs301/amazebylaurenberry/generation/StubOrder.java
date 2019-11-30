package edu.wm.cs.cs301.amazebylaurenberry.generation;

public class StubOrder implements Order{
	private int skill;
	private Builder builder;
	private boolean perfect;
	private Maze maze;
	int percentDone;
	
	public StubOrder(int skill, Builder builder, boolean perfect){
		this.skill = skill;
		this.builder = builder;
		this.perfect= perfect;
	}
	
	@Override
	public int getSkillLevel() {
		return skill;
	}
	
	@Override
	public Builder getBuilder() {
		return builder;
	}
	
	@Override
	public boolean isPerfect() {
		return perfect;
	}
	
	@Override
	public void deliver(Maze maze) {
		this.maze = maze;
	}
	
	@Override
	public void updateProgress(int percentage) {
		this.percentDone = percentage;
	}
	
	public Maze getConfiguration(){
		return maze;
	}
}