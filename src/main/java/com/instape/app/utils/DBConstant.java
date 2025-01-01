package com.instape.app.utils;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 12-Mar-2024
 * @ModifyDate - 12-Mar-2024
 * @Desc -
 */
public class DBConstant {
	public static final String generateaadharotp = "generateaadharotp";
	public static final String aadharotpvalidate = "aadharotpvalidate";
	public static final String panvalidate = "panvalidate";
	public static final String facematch = "facematch";
	public static final String aadharref = "aadharref";
	public static final String panempnamematch = "panempnamematch";
	public static final String aml = "aml";
	public static final String pennydrop = "pennydrop";
	public static final String panpennynamematch = "panpennynamematch";
	public static final String panaadharnamematch = "panaadharnamematch";
	public static final String retaildedup = "retaildedup";
	public static final String cif = "cif";
	public static final String dms = "dms";
	public static final String nameconsentdedup = "nameconsentdedup";
	public static final String nameconsentdms = "nameconsentdms";
	public static final String pennydroprevalidate = "pennydroprevalidate";
	public static final String panpennynamematchrevalidate = "panpennynamematchrevalidate";
	public static final List<String> ONBOARDINGFUNCTIONS = Arrays.asList(generateaadharotp, aadharotpvalidate, panvalidate, facematch, aadharref, panempnamematch, aml, pennydrop, panpennynamematch, panaadharnamematch, retaildedup, cif, dms, nameconsentdedup, nameconsentdms, pennydroprevalidate, panpennynamematchrevalidate);
}
