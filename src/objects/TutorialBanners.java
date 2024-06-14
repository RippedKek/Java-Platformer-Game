package objects;

public class TutorialBanners extends GameObject{

	public TutorialBanners(int x, int y, int objType) {
		super(x, y, objType);
		createHitbox();
	}
	
	private void createHitbox() {
		initHitbox(448,228);
	}

}
