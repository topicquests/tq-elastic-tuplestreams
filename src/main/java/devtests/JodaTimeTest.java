/*
 * Copyright 2017, TopicQuests
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */


package devtests;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.topicquests.support.util.DateUtil;
//import org.joda.time.Chronology;
//import org.joda.time.DateTime;
//import org.joda.time.chrono.GJChronology;
import org.topicquests.es.util.UniversalTimeStamp;

/**
 * @author jackpark
 *
 */
public class JodaTimeTest {
	private UniversalTimeStamp ts;
	/**
	 * 
	 */
	public JodaTimeTest() {
		ts = new UniversalTimeStamp();
/*		Chronology chrono = GJChronology.getInstance();
		DateTime dt = new DateTime(1066, 10, 14, 10, 0, 0, 0, chrono);
		System.out.println("1066 "+dt.getMillis()+" "+dt.getEra());
		
		new DateTime(1066, 10, 14, 10, 0, 0, 0, chrono);
		System.out.println("-1066 "+dt.getMillis()+" "+dt.getEra());
		*/
		
		String inputA = "AD 2012-01-31 23:59:59";
		String inputB = "BC 2012-01-31 23:59:59";
		
		SimpleDateFormat df = new SimpleDateFormat( "G yyyy-MM-dd HH:mm:ss" );
		try {
		Date result = df.parse( inputA );
		System.out.println("AD "+result.getTime());
		result = df.parse( inputB );
		System.out.println("BC "+result.getTime());
		long x;
		x = ts.getTimeStamp(true, 3000, 1, 31, 23, 59, 59);
		System.out.println("AD+ 3000 "+ x+ " "+
				ts.timeStampToString(x));
		x = ts.getTimeStamp(true, 2000, 1, 31, 23, 59, 59);
		System.out.println("AD+ 2000 "+ x+ " "+
				ts.timeStampToString(x));
		x = ts.getTimeStamp(true, 1940, 1, 31, 23, 59, 59);
		System.out.println("AD+ 1940 "+x+ " "+
				ts.timeStampToString(x));
		x= ts.getTimeStamp(false, 1940, 1, 31, 23, 59, 59);
		System.out.println("BC+ 1940 "+x+ " "+
				ts.timeStampToString(x));
		x = ts.getTimeStamp(false, 5000, 1, 31, 23, 59, 59);
		System.out.println("BC+ 5000 "+x+ " "+
				ts.timeStampToString(x));
		x = ts.getTimeStamp(false, 10000, 1, 31, 23, 59, 59);
		System.out.println("BC+ 10000 "+ x+ " "+
				ts.timeStampToString(x));
		x = ts.getTimeStamp(false, 15000, 1, 31, 23, 59, 59);
		System.out.println("BC+ 15000 "+ x + " "+
				ts.timeStampToString(x));
		x = ts.getTimeStamp(false, 9000000, 1, 31, 23, 59, 59);
		System.out.println("BC+ 9000000 "+ x + " "+
				ts.timeStampToString(x));
		x = ts.getTimeStamp(false, 90000000, 1, 31, 23, 59, 59);
		System.out.println("BC+ 90000000 "+ x + " "+
				ts.timeStampToString(x));
		x = ts.getTimeStamp(false, 100000000, 1, 31, 23, 59, 59);
		System.out.println("BC+ 100000000 "+ x + " "+
				ts.timeStampToString(x));
		x = ts.getTimeStamp(false, 200000000, 1, 31, 23, 59, 59);
		System.out.println("BC+ 200000000 "+ x + " "+
				ts.timeStampToString(x));
		x = ts.getTimeStamp(false, 300000000, 1, 31, 23, 59, 59);
		System.out.println("BC+ 300000000 "+ x + " "+
				ts.timeStampToString(x));
		x = ts.getTimeStamp(false, 900000000, 1, 31, 23, 59, 59);
		System.out.println("BC+ 900000000 "+ x + " "+
				ts.timeStampToString(x));
		
		} catch (Exception e) {e.printStackTrace();}
	}
	/**
	 	AD 1328083199000
		BC -125626953601000
		AD+ -944064001000
		BC+ -123354806401000
	 */
}
