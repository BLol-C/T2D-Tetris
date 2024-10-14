package tetris;
public class Piece {
	// constants for the starting position for each piece
	private final Point[][] pieces = {{mp(0, 4), mp(0, 5), mp(1, 4), mp(1, 5)},
								 {mp(1, 3), mp(1, 4), mp(1, 5), mp(1, 6)},
								 {mp(0, 3), mp(1, 3), mp(1, 4), mp(1, 5)},
								 {mp(0, 5), mp(1, 3), mp(1, 4), mp(1, 5)},
								 {mp(1, 3), mp(1, 4), mp(0, 4), mp(0, 5)},
								 {mp(0, 3), mp(0, 4), mp(1, 4), mp(1, 5)},
								 {mp(1, 3), mp(1, 4), mp(1, 5), mp(0, 4)}};
	// auxiliary method to create a new Point object
	private Point mp (int x, int y) {
		return new Point(x, y);
	}
	@Deprecated
	// generates a random piece -- not true to the mechanics behind tetris
	public Active getActive () {
		int id = (int)(Math.random()*7);
		Point[] newPiece = new Point[4];
		for (int i = 0; i < 4; i++)
			newPiece[i] = new Point(pieces[id][i].r, pieces[id][i].c);
		return new Active(newPiece, id+1);
	}
	// returns a piece with a specific id
	public Active getActive (int id) {
		Point[] newPiece = new Point[4];
		for (int i = 0; i < 4; i++)
			newPiece[i] = new Point(pieces[id][i].r, pieces[id][i].c);
		return new Active(newPiece, id+1);
	}
	// generates a permutation of the seven pieces and returns it
	public int[] getPermutation () {
		int[] res = new int[7];
		for (int i = 0; i < 7; i++)
			res[i] = i;
		permute(0, res);
		return res;
	}
	// auxiliary function to permute the pieces
	private void permute (int i, int[] a) {
		if (i == 6)
			return;
		int swap = (int)(Math.random()*(6-i) + i + 1);
		int temp = a[i];
		a[i] = a[swap];
		a[swap] = temp;
		permute(i+1, a);
	}
	// represents the active piece
	static class Active {
		Point[] pos;
		int id;
		int lor, hir, loc, hic;
		int state = 0;
		Active (Point[] pos, int id) {
			this.pos = pos;
			this.id = id;
			if (id != 2) {
				lor = 0; hir = 2;
				loc = 3; hic = 5;
			} else {
				lor = 0; hir = 3;
				loc = 3; hic = 6;
			}
		}
	}
	// represents a point on the grid
	static class Point {
		int r, c;
		Point (int r, int c) {
			this.r = r;
			this.c = c;
		}
	}
}
