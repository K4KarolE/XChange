/*  From: https://www.exchangerate-api.com/docs/supported-currencies

    Python script used to compile the hashmap body:
    ccode = """
    AED
    AFN
    ..
    """

    country = """
    United Arab Emirates
    Afghanistan
    ..
    """

    for index, item  in enumerate(ccode.split()):
        key = f'{country.split("\n")[index+1]} - {item}'
        print(f'hashMap.put("{key}","{item}");')
 */

package karole;

import java.util.HashMap;

public class CurrencyMap {
    
    public static HashMap<String, String> createCurrencyMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("United Arab Emirates - AED","AED");
        hashMap.put("Afghanistan - AFN","AFN");
        hashMap.put("Albania - ALL","ALL");
        hashMap.put("Armenia - AMD","AMD");
        hashMap.put("Netherlands Antilles - ANG","ANG");
        hashMap.put("Angola - AOA","AOA");
        hashMap.put("Argentina - ARS","ARS");
        hashMap.put("Australia - AUD","AUD");
        hashMap.put("Aruba - AWG","AWG");
        hashMap.put("Azerbaijan - AZN","AZN");
        hashMap.put("Bosnia and Herzegovina - BAM","BAM");
        hashMap.put("Barbados - BBD","BBD");
        hashMap.put("Bangladesh - BDT","BDT");
        hashMap.put("Bulgaria - BGN","BGN");
        hashMap.put("Bahrain - BHD","BHD");
        hashMap.put("Burundi - BIF","BIF");
        hashMap.put("Bermuda - BMD","BMD");
        hashMap.put("Brunei - BND","BND");
        hashMap.put("Bolivia - BOB","BOB");
        hashMap.put("Brazil - BRL","BRL");
        hashMap.put("Bahamas - BSD","BSD");
        hashMap.put("Bhutan - BTN","BTN");
        hashMap.put("Botswana - BWP","BWP");
        hashMap.put("Belarus - BYN","BYN");
        hashMap.put("Belize - BZD","BZD");
        hashMap.put("Canada - CAD","CAD");
        hashMap.put("Democratic Republic of the Congo - CDF","CDF");
        hashMap.put("Switzerland - CHF","CHF");
        hashMap.put("Chile - CLP","CLP");
        hashMap.put("China - CNY","CNY");
        hashMap.put("Colombia - COP","COP");
        hashMap.put("Costa Rica - CRC","CRC");
        hashMap.put("Cuba - CUP","CUP");
        hashMap.put("Cape Verde - CVE","CVE");
        hashMap.put("Czech Republic - CZK","CZK");
        hashMap.put("Djibouti - DJF","DJF");
        hashMap.put("Denmark - DKK","DKK");
        hashMap.put("Dominican Republic - DOP","DOP");
        hashMap.put("Algeria - DZD","DZD");
        hashMap.put("Egypt - EGP","EGP");
        hashMap.put("Eritrea - ERN","ERN");
        hashMap.put("Ethiopia - ETB","ETB");
        hashMap.put("European Union - EUR","EUR");
        hashMap.put("Fiji - FJD","FJD");
        hashMap.put("Falkland Islands - FKP","FKP");
        hashMap.put("Faroe Islands - FOK","FOK");
        hashMap.put("United Kingdom - GBP","GBP");
        hashMap.put("Georgia - GEL","GEL");
        hashMap.put("Guernsey - GGP","GGP");
        hashMap.put("Ghana - GHS","GHS");
        hashMap.put("Gibraltar - GIP","GIP");
        hashMap.put("The Gambia - GMD","GMD");
        hashMap.put("Guinea - GNF","GNF");
        hashMap.put("Guatemala - GTQ","GTQ");
        hashMap.put("Guyana - GYD","GYD");
        hashMap.put("Hong Kong - HKD","HKD");
        hashMap.put("Honduras - HNL","HNL");
        hashMap.put("Croatia - HRK","HRK");
        hashMap.put("Haiti - HTG","HTG");
        hashMap.put("Hungary - HUF","HUF");
        hashMap.put("Indonesia - IDR","IDR");
        hashMap.put("Israel - ILS","ILS");
        hashMap.put("Isle of Man - IMP","IMP");
        hashMap.put("India - INR","INR");
        hashMap.put("Iraq - IQD","IQD");
        hashMap.put("Iran - IRR","IRR");
        hashMap.put("Iceland - ISK","ISK");
        hashMap.put("Jersey - JEP","JEP");
        hashMap.put("Jamaica - JMD","JMD");
        hashMap.put("Jordan - JOD","JOD");
        hashMap.put("Japan - JPY","JPY");
        hashMap.put("Kenya - KES","KES");
        hashMap.put("Kyrgyzstan - KGS","KGS");
        hashMap.put("Cambodia - KHR","KHR");
        hashMap.put("Kiribati - KID","KID");
        hashMap.put("Comoros - KMF","KMF");
        hashMap.put("South Korea - KRW","KRW");
        hashMap.put("Kuwait - KWD","KWD");
        hashMap.put("Cayman Islands - KYD","KYD");
        hashMap.put("Kazakhstan - KZT","KZT");
        hashMap.put("Laos - LAK","LAK");
        hashMap.put("Lebanon - LBP","LBP");
        hashMap.put("Sri Lanka - LKR","LKR");
        hashMap.put("Liberia - LRD","LRD");
        hashMap.put("Lesotho - LSL","LSL");
        hashMap.put("Libya - LYD","LYD");
        hashMap.put("Morocco - MAD","MAD");
        hashMap.put("Moldova - MDL","MDL");
        hashMap.put("Madagascar - MGA","MGA");
        hashMap.put("North Macedonia - MKD","MKD");
        hashMap.put("Myanmar - MMK","MMK");
        hashMap.put("Mongolia - MNT","MNT");
        hashMap.put("Macau - MOP","MOP");
        hashMap.put("Mauritania - MRU","MRU");
        hashMap.put("Mauritius - MUR","MUR");
        hashMap.put("Maldives - MVR","MVR");
        hashMap.put("Malawi - MWK","MWK");
        hashMap.put("Mexico - MXN","MXN");
        hashMap.put("Malaysia - MYR","MYR");
        hashMap.put("Mozambique - MZN","MZN");
        hashMap.put("Namibia - NAD","NAD");
        hashMap.put("Nigeria - NGN","NGN");
        hashMap.put("Nicaragua - NIO","NIO");
        hashMap.put("Norway - NOK","NOK");
        hashMap.put("Nepal - NPR","NPR");
        hashMap.put("New Zealand - NZD","NZD");
        hashMap.put("Oman - OMR","OMR");
        hashMap.put("Panama - PAB","PAB");
        hashMap.put("Peru - PEN","PEN");
        hashMap.put("Papua New Guinea - PGK","PGK");
        hashMap.put("Philippines - PHP","PHP");
        hashMap.put("Pakistan - PKR","PKR");
        hashMap.put("Poland - PLN","PLN");
        hashMap.put("Paraguay - PYG","PYG");
        hashMap.put("Qatar - QAR","QAR");
        hashMap.put("Romania - RON","RON");
        hashMap.put("Serbia - RSD","RSD");
        hashMap.put("Russia - RUB","RUB");
        hashMap.put("Rwanda - RWF","RWF");
        hashMap.put("Saudi Arabia - SAR","SAR");
        hashMap.put("Solomon Islands - SBD","SBD");
        hashMap.put("Seychelles - SCR","SCR");
        hashMap.put("Sudan - SDG","SDG");
        hashMap.put("Sweden - SEK","SEK");
        hashMap.put("Singapore - SGD","SGD");
        hashMap.put("Saint Helena - SHP","SHP");
        hashMap.put("Sierra Leone - SLE","SLE");
        hashMap.put("Somalia - SOS","SOS");
        hashMap.put("Suriname - SRD","SRD");
        hashMap.put("South Sudan - SSP","SSP");
        hashMap.put("São Tomé and Príncipe - STN","STN");
        hashMap.put("Syria - SYP","SYP");
        hashMap.put("Eswatini - SZL","SZL");
        hashMap.put("Thailand - THB","THB");
        hashMap.put("Tajikistan - TJS","TJS");
        hashMap.put("Turkmenistan - TMT","TMT");
        hashMap.put("Tunisia - TND","TND");
        hashMap.put("Tonga - TOP","TOP");
        hashMap.put("Turkey - TRY","TRY");
        hashMap.put("Trinidad and Tobago - TTD","TTD");
        hashMap.put("Tuvalu - TVD","TVD");
        hashMap.put("Taiwan - TWD","TWD");
        hashMap.put("Tanzania - TZS","TZS");
        hashMap.put("Ukraine - UAH","UAH");
        hashMap.put("Uganda - UGX","UGX");
        hashMap.put("United States - USD","USD");
        hashMap.put("Uruguay - UYU","UYU");
        hashMap.put("Uzbekistan - UZS","UZS");
        hashMap.put("Venezuela - VES","VES");
        hashMap.put("Vietnam - VND","VND");
        hashMap.put("Vanuatu - VUV","VUV");
        hashMap.put("Samoa - WST","WST");
        hashMap.put("CEMAC - XAF","XAF");
        hashMap.put("Organisation of Eastern Caribbean States - XCD","XCD");
        hashMap.put("International Monetary Fund - XDR","XDR");
        hashMap.put("CFA - XOF","XOF");
        hashMap.put("Collectivités d'Outre-Mer - XPF","XPF");
        hashMap.put("Yemen - YER","YER");
        hashMap.put("South Africa - ZAR","ZAR");
        hashMap.put("Zambia - ZMW","ZMW");
        hashMap.put("Zimbabwe - ZWL","ZWL");
        return hashMap;
    }
}
