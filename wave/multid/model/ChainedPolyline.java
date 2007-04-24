package wave.multid.model;

import java.util.*;
import wave.util.*;

public class ChainedPolyline {

	private LinkedList pointsList = new LinkedList();
	private int index = 0;
	
	public ChainedPolyline(int index) {
		super();
		this.index = index;
	}
	
	public void addSegment(PointNDimension p1, PointNDimension p2) throws SegmentAlreadyInList,
																		  SegmentCanNotJoin,
																		  SegmentDegradesPolyline,
																		  SegmentCiclesPolyline {
	
		if (pointsList.size() == 0) {
			pointsList.add(p1);
			pointsList.add(p2);
		} else { 			
			PointNDimension first = this.getFirst();
			PointNDimension last = this.getLast();
			
			boolean firstTest = false;
			boolean lastTest = false;
			
			boolean isP1First = false;
			boolean isP2First = false;
			boolean isP1Last  = false;
			boolean isP2Last  = false;
			
			try {
				
				isP1First = first.equals(p1);
				isP2First = first.equals(p2);
				isP1Last =  last.equals(p1);
				isP2Last =  last.equals(p2);
				
				firstTest 	= (isP1First || isP2First);
				lastTest	= (isP1Last  || isP2Last);
				
			} catch (Exception e) {
				
			}
						
			if((pointsList.contains(p1) && pointsList.contains(p2))) {				
				int positionP1 = pointsList.indexOf(p1);
				int positionP2 = pointsList.indexOf(p2);
				
				if (positionP1 == 0) {
					pointsList.addFirst(p2);
					throw new SegmentCiclesPolyline(index);
				}  else if (positionP2 == 0) {
					pointsList.addFirst(p1);
					throw new SegmentCiclesPolyline(index);
				} else if (positionP1 == (pointsList.size() - 1)) {
					pointsList.addLast(p2);
					throw new SegmentCiclesPolyline(index);
				} else if (positionP2 == (pointsList.size() - 1)) {
					pointsList.addLast(p1);
					throw new SegmentCiclesPolyline(index);
				} else {
					throw new SegmentAlreadyInList();
				}
				
			} else {
				if(firstTest) {
					if(isP1First) {
						pointsList.addFirst(p2);
					} else {
						pointsList.addFirst(p1);
					}
				} else {
					if (lastTest) {
						if(isP1Last) {
							pointsList.addLast(p2);
						} else {
							pointsList.addLast(p1);
						}
					} else {
						if (pointsList.contains(p1) || pointsList.contains(p2)) {
							throw new SegmentDegradesPolyline();
						} else {
							throw new SegmentCanNotJoin();
						}
					}
				}
			}
		}
	}
	
	protected void invert() {
		PointNDimension[] tmp = this.getPolyLine();
		
		LinkedList tmpList = new LinkedList();
		
		for(int pont_point=(tmp.length - 1); pont_point >= 0; pont_point--) {
			tmpList.add(tmp[pont_point]);
		}
		
		pointsList = tmpList;
	}
	
	public void merge(ChainedPolyline operand) throws InvalidNumberOfPoints {
		// mandar mensagem de erro quando o merge nao for possivel
		
		if (this.pointsList.size() >= 2) {
			PointNDimension first, last;
			
			first = operand.getFirst();
			last = operand.getLast();
			
			boolean test = true;
			PointNDimension[] buffer = null;
			try {
				test = first.equals(this.getSecond());
			} catch (Exception e) {
				
			}
			if (test) {
				this.invert();
				buffer = operand.getPolyLine();
				for(int pont_point=2; pont_point < buffer.length; pont_point++) {
					pointsList.add(buffer[pont_point]);
				}
			} else {
				try {
					test = first.equals(this.getN_1());
				} catch (Exception e) {
					
				}
				if (test) {
					buffer = operand.getPolyLine();
					for(int pont_point=2; pont_point < buffer.length; pont_point++) {
						pointsList.add(buffer[pont_point]);
					}
				} else {
					try {
						test = last.equals(this.getSecond());
					} catch (Exception e) {
						
					}
					if (test) {
						this.invert();
						operand.invert();
						buffer = operand.getPolyLine();
						for(int pont_point=2; pont_point < buffer.length; pont_point++) {
							pointsList.add(buffer[pont_point]);
						}
					} else {
						operand.invert();
						buffer = operand.getPolyLine();
						for(int pont_point=2; pont_point < buffer.length; pont_point++) {
							pointsList.add(buffer[pont_point]);
						}
					}
				}
			} 
		} else {
			throw new InvalidNumberOfPoints();
		}
	}
	
	public int size() {
		return pointsList.size();
	}
	
	public PointNDimension getFirst() {
		//return (PointNDimension) ((PointNDimension) pointsList.getFirst()).clone();
		return (PointNDimension) pointsList.getFirst();
	}
	
	public PointNDimension getLast() {
		//return (PointNDimension) ((PointNDimension) pointsList.getLast()).clone();
		return (PointNDimension) pointsList.getLast();
	}
	
	public PointNDimension[] getPolyLine() {
		
		Object[] tmp = pointsList.toArray();
		
		PointNDimension[] pointsArray = new PointNDimension[tmp.length];
		
		for (int pont_point = 0; pont_point < tmp.length; pont_point++) {
			pointsArray[pont_point] = (PointNDimension) ((PointNDimension) tmp[pont_point]).clone();
		}
		
		return pointsArray;
	}
	
	private PointNDimension getSecond() {
		return (PointNDimension) pointsList.get(1);
	}
	
	private PointNDimension getN_1() {			
		return (PointNDimension) pointsList.get(pointsList.size() - 2);
	}

}

class InvalidNumberOfPoints extends Exception {
	public InvalidNumberOfPoints() {
		super("Invalid Number Of Points");
	}
}
