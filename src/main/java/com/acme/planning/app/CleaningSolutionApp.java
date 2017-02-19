package com.acme.planning.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import com.acme.planning.model.Cleaner;
import com.acme.planning.model.CleaningSolution;
import com.acme.planning.model.DayOfWeek;
import com.acme.planning.model.FinalCleaningSchedule;
import com.acme.planning.model.House;
import com.acme.planning.model.HouseCleaningSpot;
import com.acme.planning.model.Location;
import com.acme.planning.model.ShiftDate;

public class CleaningSolutionApp {

	public static final String SOLVER_CONFIG = "com/acme/planner/solver/cleaningPlanSolverConfig.xml";

	public static void main(String[] args) {

		Solver solver = new CleaningSolutionApp().createSolver();

		CleaningSolution unsolvedCleaningSolution = createCleaningSolution();
		solver.solve(unsolvedCleaningSolution);
		CleaningSolution solvedCloudBalance = (CleaningSolution) solver.getBestSolution();
		System.out.println("\nSolved CleaningProblem :\n");
		List<FinalCleaningSchedule> planningSolutionResponseObj =  getPlanningSolutionObject(solvedCloudBalance);
		System.out.println(planningSolutionResponseObj.size());
	}

	protected Solver createSolver() {
		SolverFactory solverFactory = SolverFactory
				.createFromXmlResource(SOLVER_CONFIG);
		Solver solver = solverFactory.buildSolver();
		solver.addEventListener(new SolverEventListener() {
			public void bestSolutionChanged(BestSolutionChangedEvent event) {
				CleaningSolution bestSolution = (CleaningSolution) event
						.getNewBestSolution();
			}
		});
		return solver;
	}
    public static String toDisplayString(CleaningSolution cleaningSolution) {
        StringBuilder displayString = new StringBuilder();
        String housetype;
        for (HouseCleaningSpot houseCleaningSpot : cleaningSolution.getHouseCleaningSpotList()) {
            Cleaner cleaner = houseCleaningSpot.getCleaner();
            displayString.append("  House:::").append(houseCleaningSpot.getHouse().getId()).append(" -> ")
            .append("  HCSpot:::").append(houseCleaningSpot.getCleaningSpotIndex()).append(" -> ")
            .append("  Day:::").append(houseCleaningSpot.getHouse().getDayOfWeek().getDayId()).append(" -> ")
            .append("  Cleaner:::").append(cleaner == null ? null : cleaner.getId()).append("\n");
        }
        return displayString.toString();
    }
    public static List<FinalCleaningSchedule> getPlanningSolutionObject(CleaningSolution cleaningSolution) {
    	FinalCleaningSchedule finalCleaningSchedule = null;
        List<HouseCleaningSpot> houseCleaningSpotList = cleaningSolution.getHouseCleaningSpotList();
        
        List<FinalCleaningSchedule> listHouseCleaningSpot = new ArrayList<FinalCleaningSchedule>();
        Set<Object> houseSet = new HashSet<Object>();
        Set<Object> daySet = new HashSet<Object>();
        
        for (Iterator<HouseCleaningSpot> iterator = houseCleaningSpotList.iterator(); iterator.hasNext();) {
        	finalCleaningSchedule = new FinalCleaningSchedule();
        	HouseCleaningSpot houseCleaningSpot = (HouseCleaningSpot) iterator.next();
        	finalCleaningSchedule.setHouseId(houseCleaningSpot.getHouse().getId());
        	finalCleaningSchedule.setHouseCleaningSpot(String.valueOf(houseCleaningSpot.getCleaningSpotIndex()));
        	//finalCleaningSchedule.setDayId(houseCleaningSpot.getHouse().getDayOfWeek().getDayId());
        	finalCleaningSchedule.setShiftDate(houseCleaningSpot.getHouse().getShiftDate().getWorkDate());
        	Cleaner cleaner = houseCleaningSpot.getCleaner();
        	Long cleanerId = (cleaner == null ? null : cleaner.getId());
        	if (cleanerId != null){
        		finalCleaningSchedule.setCleanerId(String.valueOf(cleaner.getId()));
        	}else {
        		finalCleaningSchedule.setCleanerId("No Cleaner Available");
        	}
        	
        	listHouseCleaningSpot.add(finalCleaningSchedule);
		}
        System.out.println("listHouseCleaningSpot ::: "+listHouseCleaningSpot.size());
        for (Iterator<FinalCleaningSchedule> iterator = listHouseCleaningSpot.iterator(); iterator.hasNext();) {
			finalCleaningSchedule = (FinalCleaningSchedule) iterator.next();
			//System.out.println(finalCleaningSchedule.toString());
        	if(!houseSet.contains(finalCleaningSchedule.getHouseId())){
        		houseSet.add(finalCleaningSchedule.getHouseId());
        		System.out.println("============================================================");
        		System.out.println("House Id is ::: "+finalCleaningSchedule.getHouseId());
        		if(!daySet.contains(finalCleaningSchedule.getDayId())){
            		daySet.add(finalCleaningSchedule.getDayId());
        		}
        		System.out.println("Cleaning spot "+finalCleaningSchedule.getHouseCleaningSpot()+" assigned to cleaner "+
        		finalCleaningSchedule.getCleanerId()+" on "+finalCleaningSchedule.getShiftDate()+
        		", "+finalCleaningSchedule.getShiftDate().getDayOfWeek()
        				);
        	}else if(houseSet.contains(finalCleaningSchedule.getHouseId())){
        		System.out.println("Cleaning spot "+finalCleaningSchedule.getHouseCleaningSpot()+" assigned to cleaner "+finalCleaningSchedule.getCleanerId()+" on "+finalCleaningSchedule.getShiftDate());
        	}
		}

        return listHouseCleaningSpot;
    }

	private static CleaningSolution createCleaningSolution() {
		CleaningSolution unsolvedCleaningProblem = new CleaningSolution();
		
		List<Cleaner> clist = createCleaners();
		List<HouseCleaningSpot> hsclist = createHouseCleaningSpot();

		unsolvedCleaningProblem.setCleanerList(clist);
		unsolvedCleaningProblem.setHouseCleaningSpotList(hsclist);

		return unsolvedCleaningProblem;

	}

	public static List<Cleaner> createCleaners() {
		// Random random = new Random(System.currentTimeMillis());
		List<Cleaner> cleanersList = new ArrayList<Cleaner>();
		
		DayOfWeek monday = new DayOfWeek("Monday");
		DayOfWeek tuesday = new DayOfWeek("Tuesday");
		DayOfWeek wednesday = new DayOfWeek("Wednesday");
		DayOfWeek thursday = new DayOfWeek("Thursday");
		DayOfWeek friday = new DayOfWeek("Friday");

		Cleaner cleaner1 = new Cleaner(new Long(111), monday);
		Location location1 = new Location();
		location1.setDistance(1);
		cleaner1.setCleanerLocation(location1);
		cleaner1.setMaxWorkHours(40);
		cleaner1.setDailyWorkHours(10);
		
		Cleaner cleaner2 = new Cleaner(new Long(112), tuesday);
		Location location2 = new Location();
		location2.setDistance(2);
		cleaner2.setCleanerLocation(location2);
		cleaner2.setMaxWorkHours(40);
		cleaner2.setDailyWorkHours(10);
		
		Cleaner cleaner3 = new Cleaner(new Long(113), wednesday);
		Location location3 = new Location();
		location3.setDistance(3);
		cleaner3.setCleanerLocation(location3);
		cleaner3.setMaxWorkHours(40);
		cleaner3.setDailyWorkHours(8);
		
		Cleaner cleaner4 = new Cleaner(new Long(114), wednesday);
		Location location4 = new Location();
		location4.setDistance(4);
		cleaner4.setCleanerLocation(location4);
		cleaner4.setMaxWorkHours(40);
		cleaner4.setDailyWorkHours(8);
		
		Cleaner cleaner5 = new Cleaner(new Long(115), wednesday);
		Location location5 = new Location();
		location5.setDistance(5);
		cleaner5.setCleanerLocation(location5);
		cleaner5.setMaxWorkHours(40);
		cleaner5.setDailyWorkHours(8);
		
		Cleaner cleaner6 = new Cleaner(new Long(116), wednesday);
		Location location6 = new Location();
		location6.setDistance(6);
		cleaner6.setCleanerLocation(location6);
		cleaner6.setMaxWorkHours(40);
		cleaner6.setDailyWorkHours(8);
		
		Cleaner cleaner7 = new Cleaner(new Long(117), thursday);
		Location location7 = new Location();
		location7.setDistance(7);
		cleaner7.setCleanerLocation(location7);
		cleaner7.setMaxWorkHours(40);
		cleaner7.setDailyWorkHours(8);
		
		Cleaner cleaner8 = new Cleaner(new Long(118), thursday);
		Location location8 = new Location();
		location8.setDistance(8);
		cleaner8.setCleanerLocation(location8);
		cleaner8.setMaxWorkHours(40);
		cleaner8.setDailyWorkHours(8);
		
		cleanersList.add(cleaner1);
		cleanersList.add(cleaner2);
		cleanersList.add(cleaner3);
		cleanersList.add(cleaner4);
		cleanersList.add(cleaner5);
		cleanersList.add(cleaner6);
		//cleanersList.add(cleaner7);
		//cleanersList.add(cleaner8);
		System.out.println("Size of the cleaners list is ::: "+cleanersList.size());

		return cleanersList;
	}

	public static List<HouseCleaningSpot> createHouseCleaningSpot() {
		// Random random = new Random(System.currentTimeMillis());
		LocalDate today = LocalDate.now();
		List<HouseCleaningSpot> houseCleaningSpotList = new ArrayList<HouseCleaningSpot>();
		
		//ShiftDate(String shiftId, LocalDate workDate, DayOfWeek dow)
		ShiftDate shiftDate1 = new ShiftDate("001", today, today.getDayOfWeek());
		ShiftDate shiftDate2 = new ShiftDate("002", today.plusDays(1), today.getDayOfWeek());
		ShiftDate shiftDate3 = new ShiftDate("003", today.plusDays(2), today.getDayOfWeek());
		ShiftDate shiftDate4 = new ShiftDate("004", today.plusDays(3), today.getDayOfWeek());
		ShiftDate shiftDate5 = new ShiftDate("005", today.plusDays(4), today.getDayOfWeek());
		ShiftDate shiftDate6 = new ShiftDate("006", today.plusDays(5), today.getDayOfWeek());
		ShiftDate shiftDate7 = new ShiftDate("007", today.plusDays(6), today.getDayOfWeek());
		ShiftDate shiftDate8 = new ShiftDate("008", today.plusDays(7), today.getDayOfWeek());
		ShiftDate shiftDate9 = new ShiftDate("009", today.plusDays(8), today.getDayOfWeek());
		ShiftDate shiftDate10 = new ShiftDate("010", today.plusDays(9), today.getDayOfWeek());
		ShiftDate shiftDate11 = new ShiftDate("011", today.plusDays(10), today.getDayOfWeek());
		ShiftDate shiftDate12 = new ShiftDate("012", today.plusDays(11), today.getDayOfWeek());
		ShiftDate shiftDate13 = new ShiftDate("013", today.plusDays(12), today.getDayOfWeek());
		ShiftDate shiftDate14 = new ShiftDate("014", today.plusDays(13), today.getDayOfWeek());
		ShiftDate shiftDate15 = new ShiftDate("015", today.plusDays(14), today.getDayOfWeek());
		
		//DayOfWeek dayOfWeek1 = new DayOfWeek(new Long(1), "Monday");
		House house1 = new House(new Long(1111), "A");
		Location location1 = new Location();
		location1.setDistance(1);
		house1.setHouseLocation(location1);
		house1.setShiftDate(shiftDate1);
		HouseCleaningSpot houseCleaningSpot11 = new HouseCleaningSpot(new Long(11),house1, 101);
		HouseCleaningSpot houseCleaningSpot12 = new HouseCleaningSpot(new Long(12),house1, 102);
		HouseCleaningSpot houseCleaningSpot13 = new HouseCleaningSpot(new Long(13),house1, 103);
		
		
		//DayOfWeek dayOfWeek2 = new DayOfWeek(new Long(2), "Tuesday");
		House house2 = new House(new Long(2222), "B");
		Location location2 = new Location();
		location2.setDistance(2);
		house2.setHouseLocation(location2);
		house2.setShiftDate(shiftDate2);
		HouseCleaningSpot houseCleaningSpot21 = new HouseCleaningSpot(new Long(21),house2, 201);
		HouseCleaningSpot houseCleaningSpot22 = new HouseCleaningSpot(new Long(22),house2, 202);
	

		//DayOfWeek dayOfWeek3 = new DayOfWeek(new Long(3), "Wednesday");
		House house3 = new House(new Long(3333), "C");
		Location location3 = new Location();
		location3.setDistance(3);
		house3.setHouseLocation(location3);
		house3.setShiftDate(shiftDate3);
		HouseCleaningSpot houseCleaningSpot31 = new HouseCleaningSpot(new Long(31),house3, 301);
		HouseCleaningSpot houseCleaningSpot32 = new HouseCleaningSpot(new Long(32),house3, 302);
		HouseCleaningSpot houseCleaningSpot33 = new HouseCleaningSpot(new Long(33),house3, 303);
		HouseCleaningSpot houseCleaningSpot34 = new HouseCleaningSpot(new Long(34),house3, 304);
		HouseCleaningSpot houseCleaningSpot35 = new HouseCleaningSpot(new Long(35),house3, 305);

		
		//DayOfWeek dayOfWeek4 = new DayOfWeek(new Long(4), "Thursday");
		House house4 = new House(new Long(4444), "D");
		Location location4 = new Location();
		location4.setDistance(4);
		house4.setHouseLocation(location4);
		house4.setShiftDate(shiftDate4);
		HouseCleaningSpot houseCleaningSpot41 = new HouseCleaningSpot(new Long(41),house4, 401);
		HouseCleaningSpot houseCleaningSpot42 = new HouseCleaningSpot(new Long(42),house4, 402);
		
		//DayOfWeek dayOfWeek5 = new DayOfWeek(new Long(5), "Friday");
		House house5 = new House(new Long(5555), "E");
		Location location5 = new Location();
		location5.setDistance(5);
		house5.setHouseLocation(location5);
		house5.setShiftDate(shiftDate5);
		HouseCleaningSpot houseCleaningSpot51 = new HouseCleaningSpot(new Long(51),house5, 501);
		HouseCleaningSpot houseCleaningSpot52 = new HouseCleaningSpot(new Long(52),house5, 502);
		HouseCleaningSpot houseCleaningSpot53 = new HouseCleaningSpot(new Long(53),house5, 503);
		
		House house6 = new House(new Long(6666), "F");
		Location location6 = new Location();
		location6.setDistance(6);
		house6.setHouseLocation(location6);
		house6.setShiftDate(shiftDate3);
		HouseCleaningSpot houseCleaningSpot61 = new HouseCleaningSpot(new Long(61),house6, 601);
		HouseCleaningSpot houseCleaningSpot62 = new HouseCleaningSpot(new Long(62),house6, 602);
		HouseCleaningSpot houseCleaningSpot63 = new HouseCleaningSpot(new Long(63),house6, 603);
		
				
		House house7 = new House(new Long(7777), "G");
		House house8 = new House(new Long(8888), "H");
		House house9 = new House(new Long(9999), "I");
		House house10 = new House(new Long(10000), "J");
		House house11 = new House(new Long(10001), "K");
		House house12 = new House(new Long(10002), "L");
		House house13 = new House(new Long(10003), "M");
		
		Location location7 = new Location();
		location7.setDistance(7);
		
		Location location8 = new Location();
		location8.setDistance(8);
		
		Location location9 = new Location();
		location9.setDistance(9);
		
		Location location10 = new Location();
		location10.setDistance(10);
		
		Location location11 = new Location();
		location11.setDistance(11);
		
		Location location12 = new Location();
		location12.setDistance(12);
		
		Location location13 = new Location();
		location13.setDistance(13);
		
		house7.setShiftDate(shiftDate6);
		house8.setShiftDate(shiftDate7);
		house9.setShiftDate(shiftDate8);
		house10.setShiftDate(shiftDate9);
		house11.setShiftDate(shiftDate10);
		house12.setShiftDate(shiftDate11);
		house13.setShiftDate(shiftDate12);
		
		house7.setHouseLocation(location7);
		house8.setHouseLocation(location8);
		house9.setHouseLocation(location9);
		house10.setHouseLocation(location10);
		house11.setHouseLocation(location11);
		house12.setHouseLocation(location12);
		house13.setHouseLocation(location13);

		
		

				HouseCleaningSpot houseCleaningSpot71 = new HouseCleaningSpot(new Long(71),house7, 701);
				HouseCleaningSpot houseCleaningSpot72 = new HouseCleaningSpot(new Long(72),house7, 702);
				HouseCleaningSpot houseCleaningSpot73 = new HouseCleaningSpot(new Long(73),house7, 703);
				

				HouseCleaningSpot houseCleaningSpot81 = new HouseCleaningSpot(new Long(81),house8, 801);
				HouseCleaningSpot houseCleaningSpot82 = new HouseCleaningSpot(new Long(82),house8, 802);
				HouseCleaningSpot houseCleaningSpot83 = new HouseCleaningSpot(new Long(83),house8, 803);
				

				HouseCleaningSpot houseCleaningSpot91 = new HouseCleaningSpot(new Long(91),house9, 901);
				HouseCleaningSpot houseCleaningSpot92 = new HouseCleaningSpot(new Long(92),house9, 902);
				HouseCleaningSpot houseCleaningSpot93 = new HouseCleaningSpot(new Long(93),house9, 903);
				

				HouseCleaningSpot houseCleaningSpot101 = new HouseCleaningSpot(new Long(101),house10, 1001);
				HouseCleaningSpot houseCleaningSpot102 = new HouseCleaningSpot(new Long(102),house10, 1002);
				HouseCleaningSpot houseCleaningSpot103 = new HouseCleaningSpot(new Long(103),house10, 1003);
				

				HouseCleaningSpot houseCleaningSpot111 = new HouseCleaningSpot(new Long(104),house11, 1101);
				HouseCleaningSpot houseCleaningSpot112 = new HouseCleaningSpot(new Long(105),house11, 1102);
				HouseCleaningSpot houseCleaningSpot113 = new HouseCleaningSpot(new Long(106),house11, 1103);
				

				HouseCleaningSpot houseCleaningSpot121 = new HouseCleaningSpot(new Long(107),house12, 1201);
				HouseCleaningSpot houseCleaningSpot122 = new HouseCleaningSpot(new Long(108),house12, 1202);
				HouseCleaningSpot houseCleaningSpot123 = new HouseCleaningSpot(new Long(109),house12, 1203);
		
		
		
		
		
		houseCleaningSpotList.add(houseCleaningSpot11);
		houseCleaningSpotList.add(houseCleaningSpot12);
		houseCleaningSpotList.add(houseCleaningSpot13);
		
		houseCleaningSpotList.add(houseCleaningSpot21);
		houseCleaningSpotList.add(houseCleaningSpot22);
		
		houseCleaningSpotList.add(houseCleaningSpot31);
		houseCleaningSpotList.add(houseCleaningSpot32);
		houseCleaningSpotList.add(houseCleaningSpot33);
		houseCleaningSpotList.add(houseCleaningSpot34);
		houseCleaningSpotList.add(houseCleaningSpot35);
		
		houseCleaningSpotList.add(houseCleaningSpot41);
		houseCleaningSpotList.add(houseCleaningSpot42);
		
		houseCleaningSpotList.add(houseCleaningSpot51);
		houseCleaningSpotList.add(houseCleaningSpot52);
		houseCleaningSpotList.add(houseCleaningSpot53);
		
		houseCleaningSpotList.add(houseCleaningSpot61);
		houseCleaningSpotList.add(houseCleaningSpot62);
		houseCleaningSpotList.add(houseCleaningSpot63);

		houseCleaningSpotList.add(houseCleaningSpot71);
		houseCleaningSpotList.add(houseCleaningSpot72);
		houseCleaningSpotList.add(houseCleaningSpot73);
		
		houseCleaningSpotList.add(houseCleaningSpot81);
		houseCleaningSpotList.add(houseCleaningSpot82);
		houseCleaningSpotList.add(houseCleaningSpot83);
		
		houseCleaningSpotList.add(houseCleaningSpot91);
		houseCleaningSpotList.add(houseCleaningSpot92);
		houseCleaningSpotList.add(houseCleaningSpot93);
		
		houseCleaningSpotList.add(houseCleaningSpot101);
		houseCleaningSpotList.add(houseCleaningSpot102);
		houseCleaningSpotList.add(houseCleaningSpot103);
		
		houseCleaningSpotList.add(houseCleaningSpot111);
		houseCleaningSpotList.add(houseCleaningSpot112);
		houseCleaningSpotList.add(houseCleaningSpot113);
		
		houseCleaningSpotList.add(houseCleaningSpot121);
		houseCleaningSpotList.add(houseCleaningSpot122);
		houseCleaningSpotList.add(houseCleaningSpot123);
		

		
		

		
		//System.out.println("Size of the houseCleaningSpotList list is ::: "+houseCleaningSpotList.size());

		return houseCleaningSpotList;
	}
}
