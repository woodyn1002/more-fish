package me.elsiff.morefish;

public class CaughtFish extends CustomFish {
	private final double length;

	public CaughtFish(CustomFish fish, double length) {
		super(fish.getName(), fish.getLore(), fish.getLengthMin(), fish.getLengthMax(), fish.getIcon(), fish.getCommands(), fish.getRarity());

		this.length = length;
	}

	public double getLength() {
		return length;
	}
}
