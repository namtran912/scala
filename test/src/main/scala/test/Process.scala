package test

import java.io.{BufferedReader, FileWriter, InputStreamReader}
import java.net.URL

import scala.util.control.Breaks._

/**
  * Created by CPU11179-local on 7/20/2016.
  */
object Process {
    final val StrFile: String = "IP-COUNTRY-REGION-CITY-ISP.CSV"
    final val StrFileModified: String = "ip-modified.csv"
    final val StrFileLog: String = "mapping-log.csv"

    final val ProfileLocationID_2_Area: Map[Int, String] = Map(841->"An Giang", 842->"Ba Ria-Vung Tau", 843->"Bac Giang", 844->"Bac Kan", 845->"Bac Lieu", 846->"Bac Ninh", 847->"Ben Tre", 848->"Binh Dinh",
      849->"Binh Duong", 850->"Binh Phuoc", 851->"Binh Thuan", 852->"Ca Mau", 853->"Can Tho", 854->"Cao Bang", 855->"Da Nang", 856->"Dak Lak", 857->"Dac Nong", 858->"Dien Bien", 859->"Dong Nai",
      860->"Dong Thap", 861->"Gia Lai", 862->"Ha Giang", 863->"Ha Nam", 864->"Ha Noi", 865->"Ha Tinh", 866->"Hai Duong", 867->"Hai Phong", 868->"Hau Giang", 869->"Hoa Binh", 870->"Hung Yen",
      871->"Khanh Hoa", 872->"Kien Giang", 873->"Kon Tum", 874->"Lai Chau", 875->"Lam Dong", 876->"Lang Son", 877->"Lao Cai", 878->"Long An", 879->"Nam Dinh", 880->"Nghe An", 881->"Ninh Binh",
      882->"Ninh Thuan", 883->"Phu Tho", 884->"Phu Yen", 885->"Quang Binh", 886->"Quang Nam", 887->"Quang Ngai", 888->"Quang Ninh", 889->"Quang Tri", 890->"Ho Chi Minh", 891->"Soc Trang", 892->"Son La",
      893->"Tay Ninh", 894->"Thai Binh", 895->"Thai Nguyen", 896->"Thanh Hoa", 897->"Thua Thien Hue", 898->"Tien Giang", 899->"Tra Vinh", 900->"Tuyen Quang", 901->"Vinh Long", 902->"Vinh Phuc", 903->"Yen Bai")

    final val ProfileLocationID_2_Country: Map[Int, String] = Map(1->"Andorra",2->"United Arab Emirates",3->"Afghanistan",4->"Antigua and Barbuda",5->"Anguilla",6->"Albania",7->"Armenia",8->"Netherlands Antilles",9->"Angola",
      10->"Antarctica",11->"Argentina",12->"American Samoa",13->"Austria",14->"Australia",15->"Aruba",16->"Azerbaijan",17->"Bosnia and Herzegovina",18->"Barbados",19->"Bangladesh",20->"Belgium",21->"Burkina Faso",
      22->"Bulgaria",23->"Bahrain",24->"Burundi",25->"Benin",26->"Bermuda",27->"Brunei",28->"Bolivia",29->"Brazil",30->"Bahamas",31->"Bhutan",32->"Bouvet Island",33->"Botswana",34->"Belarus",35->"Belize",36->"Canada",
      37->"Cocos [Keeling] Islands",38->"Congo [DRC]",39->"Central African Republic",40->"Congo [Republic]",41->"Switzerland",42->"Cote d'Ivoire",43->"Cook Islands",44->"Chile",45->"Cameroon",46->"China",47->"Colombia",
      48->"Costa Rica",49->"Cuba",50->"Cape Verde",51->"Christmas Island",52->"Cyprus",53->"Czech Republic",54->"Germany",55->"Djibouti",56->"Denmark",57->"Dominica",58->"Dominican Republic",59->"Algeria",60->"Ecuador",
      61->"Estonia",62->"Egypt",63->"Western Sahara",64->"Eritrea",65->"Spain",66->"Ethiopia",67->"Finland",68->"Fiji",69->"Falkland Islands [Islas Malvinas]",70->"Micronesia",71->"Faroe Islands",72->"France",73->"Gabon",
      74->"United Kingdom",75->"Grenada",76->"Georgia",77->"French Guiana",78->"Guernsey",79->"Ghana",80->"Gibraltar",81->"Greenland",82->"Gambia",83->"Guinea",84->"Guadeloupe",85->"Equatorial Guinea",86->"Greece",
      87->"South Georgia and the South Sandwich Islands",88->"Guatemala",89->"Guam",90->"Guinea-Bissau",91->"Guyana",92->"Gaza Strip",93->"Hong Kong",94->"Heard Island and McDonald Islands",95->"Honduras",96->"Croatia",
      97->"Haiti",98->"Hungary",99->"Indonesia",100->"Ireland",101->"Israel",102->"Isle of Man",103->"India",104->"British Indian Ocean Territory",105->"Iraq",106->"Iran",107->"Iceland",108->"Italy",109->"Jersey",110->"Jamaica",
      111->"Jordan",112->"Japan",113->"Kenya",114->"Kyrgyzstan",115->"Cambodia",116->"Kiribati",117->"Comoros",118->"Saint Kitts and Nevis",119->"North Korea",120->"South Korea",121->"Kuwait",122->"Cayman Islands",123->"Kazakhstan",
      124->"Laos",125->"Lebanon",126->"Saint Lucia",127->"Liechtenstein",128->"Sri Lanka",129->"Liberia",130->"Lesotho",131->"Lithuania",132->"Luxembourg",133->"Latvia",134->"Libya",135->"Morocco",136->"Monaco",137->"Moldova",
      138->"Montenegro",139->"Madagascar",140->"Marshall Islands",141->"Macedonia [FYROM]",142->"Mali",143->"Myanmar [Burma]",144->"Mongolia",145->"Macau",146->"Northern Mariana Islands",147->"Martinique",148->"Mauritania",
      149->"Montserrat",150->"Malta",151->"Mauritius",152->"Maldives",153->"Malawi",154->"Mexico",155->"Malaysia",156->"Mozambique",157->"Namibia",158->"New Caledonia",159->"Niger",160->"Norfolk Island",161->"Nigeria",162->"Nicaragua",
      163->"Netherlands",164->"Norway",165->"Nepal",166->"Nauru",167->"Niue",168->"New Zealand",169->"Oman",170->"Panama",171->"Peru",172->"French Polynesia",173->"Papua New Guinea",174->"Philippines",175->"Pakistan",176->"Poland",
      177->"Saint Pierre and Miquelon",178->"Pitcairn Islands",179->"Puerto Rico",180->"Palestinian Territories",181->"Portugal",182->"Palau",183->"Paraguay",184->"Qatar",185->"Reunion",186->"Romania",187->"Serbia",188->"Russia",
      189->"Rwanda",190->"Saudi Arabia",191->"Solomon Islands",192->"Seychelles",193->"Sudan",194->"Sweden",195->"Singapore",196->"Saint Helena",197->"Slovenia",198->"Svalbard and Jan Mayen",199->"Slovakia",200->"Sierra Leone",
      201->"San Marino",202->"Senegal",203->"Somalia",204->"Suriname",205->"Sao Tome and Principe",206->"El Salvador",207->"Syria",208->"Swaziland",209->"Turks and Caicos Islands",210->"Chad",211->"French Southern Territories",
      212->"Togo",213->"Thailand",214->"Tajikistan",215->"Tokelau",216->"Timor-Leste",217->"Turkmenistan",218->"Tunisia",219->"Tonga",220->"Turkey",221->"Trinidad and Tobago",222->"Tuvalu",223->"Taiwan",224->"Tanzania",
      225->"Ukraine",226->"Uganda",227->"U.S. Minor Outlying Islands",228->"United States",229->"Uruguay",230->"Uzbekistan",231->"Vatican City",232->"Saint Vincent and the Grenadines",233->"Venezuela",234->"British Virgin Islands",235->"U.S. Virgin Islands",
      236->"Vietnam",237->"Vanuatu",238->"Wallis and Futuna",239->"Samoa",240->"Kosovo",241->"Yemen",242->"Mayotte",243->"South Africa",244->"Zambia",245->"Zimbabwe",246->"Aland Islands",247->"Saint Barthelemy",248->"Bonaire",249->"Curacao",250->"Saint Martin (French Part)",251->"South Sudan",252->"Sint Maarten (Dutch Part)")

    final val MappingLocationModified: Map[String, String] = Map("Thua Thien-Hue" -> "Thua Thien Hue", "Korea, Republic of" -> "South Korea", "Korea, Democratic People's Republic of" -> "North Korea", "Taiwan, Province of China" -> "Taiwan",
      "Russian Federation" -> "Russia", "Iran, Islamic Republic of" -> "Iran", "Syrian Arab Republic" -> "Syria", "Congo, The Democratic Republic of The" -> "Congo [DRC]", "Congo" -> "Congo [Republic]", "Palestine, State of" -> "Palestinian Territories",
      "Moldova, Republic of" -> "Moldova", "Macedonia, The Former Yugoslav Republic of" -> "Macedonia [FYROM]", "Virgin Islands, British" -> "British Virgin Islands", "Virgin Islands, U.S." -> "U.S. Virgin Islands", "Venezuela, Bolivarian Republic of" -> "Venezuela",
      "Macao" -> "Macau", "Brunei Darussalam" -> "Brunei", "Saint Vincent and The Grenadines" -> "Saint Vincent and the Grenadines", "Bolivia, Plurinational State of" -> "Bolivia", "Tanzania, United Republic of" -> "Tanzania", "Cote D'ivoire" -> "Cote d'Ivoire",
      "Cabo Verde" -> "Cape Verde", "Lao People's Democratic Republic" -> "Laos", "Myanmar" -> "Myanmar [Burma]", "Micronesia, Federated States of" -> "Micronesia", "Holy See" -> "Vatican City", "Falkland Islands (Malvinas)" -> "Falkland Islands [Islas Malvinas]",
      "United States Minor Outlying Islands" -> "U.S. Minor Outlying Islands", "Bonaire, Sint Eustatius and Saba" -> "Bonaire", "South Georgia and The South Sandwich Islands" -> "South Georgia and the South Sandwich Islands", "Pitcairn" -> "Pitcairn Islands")

    val reader: Reader = new Reader()

    def ip2Long(ip: String): Long = {
        val atoms: Array[Long] = ip.split("\\.").map(java.lang.Long.parseLong(_))
        val result: Long = (3 to 0 by -1).foldLeft(0L)(
            (result, position) => result | (atoms(3 - position) << position * 8))

        result & 0xFFFFFFFF
    }

    def getIP(): String  = {
        val urlIP: URL = new URL("http://checkip.amazonaws.com")
        val reader: BufferedReader = new BufferedReader(new InputStreamReader(urlIP.openStream()))
        reader.readLine
    }

    def getLocationCode(ipStr: String): Int = {
        val ip = ip2Long(ipStr)
        var left: Int = 1
        var right: Int = reader.nDatas - 1
        var id = -1

        breakable {while (left <= right) {
            val mid = (left + right) / 2
            val info0: Array[String]  = reader.data(mid - 1).split("\",\"")
            val info: Array[String]  = reader.data(mid).split("\",\"")
            val ipL = info0(0).substring(1).toLong
            val ipR = info(0).substring(1).toLong

            if (ipL < ip && ipR >= ip) {
                id = info(1).toInt
                break
            }
            else if (ipL >= ip)
                right = mid - 1
            else
                left = mid + 1

        }}

        id
    }

    def mapping(inVN: Boolean, location: String): AnyVal = {
        if (inVN) {
            val index = Process.ProfileLocationID_2_Area.values.toList.indexOf(location)
            if (index == -1)
                index
            else
                Process.ProfileLocationID_2_Area.keys.toList(index)
        }
        else {
            val index = Process.ProfileLocationID_2_Country.values.toList.indexOf(location)
            if (index == -1)
                index
            else
                Process.ProfileLocationID_2_Country.keys.toList(index)
        }
    }
}