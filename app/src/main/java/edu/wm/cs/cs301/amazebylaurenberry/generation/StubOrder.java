package edu.wm.cs.cs301.amazebylaurenberry.generation;
import android.os.Message;


import edu.wm.cs.cs301.amazebylaurenberry.GeneratingActivity;
import edu.wm.cs.cs301.amazebylaurenberry.generation.Order.Builder;

import static edu.wm.cs.cs301.amazebylaurenberry.GeneratingActivity.*;


public class StubOrder implements Order{
	private int skill;
	private Builder builder;
	private boolean perfect;
	private Maze maze;
	int percentDone ;

	public StubOrder(){
		percentDone =0;

	}
	
	public StubOrder(int skill, Builder builder, boolean perfect){
		this.skill = skill;
		this.builder = builder;
		this.perfect= perfect;
		percentDone =0;
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

		//this.percentDone = percentage;

		if (percentDone < percentage && percentage <= 100) {
			percentDone = percentage;
			Message progressMsg = new Message();
			progressMsg.arg1 = percentage;

			handler.sendMessage(progressMsg);
		}
	}
	
	public Maze getConfiguration(){
		return maze;
	}

	public void setPerfect(boolean perfect){
		this.perfect=perfect;
	}

	public void setSkill(int skill){
		this.skill=skill;
	}

	public void setBuilder(Builder builder){
		this.builder=builder;
	}


}