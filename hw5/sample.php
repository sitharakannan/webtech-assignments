<?php
    if(isset($_GET['initalTable']))
    {
        if (($_GET["keyword"] == 'f')) {
            $nameErr = "Please fill out this field";

        }
        else {

            //echo $_GET["location"];
            if (!($_GET["loc"] == 'f')) { //loc_input
                //echo "inside AGAIN";
                $address = $_GET["loc"];
                $address = str_replace(' ', '+', $address);
                $api_key = 'AIzaSyDwF0qbAiBnIfYnzY7Rukw4tJHo23sIQIQ';
                $map_url = 'https://maps.googleapis.com/maps/api/geocode/json?address=' .$address. '&key='.$api_key;
                //echo "url: ".$map_url;
                $retrieveLoc = file_get_contents("$map_url");
                //echo "$retrieveLoc";
                $json = json_decode($retrieveLoc, true);
                $lat =  $json["results"][0]["geometry"]["location"]["lat"];
                $lon =  $json["results"][0]["geometry"]["location"]["lng"];

            }
            else {
                //echo "here instead";
                $here_location = $_GET["location"];
                //echo "$here_location";
                list($lat, $lon) = preg_split('[#]', $here_location);
                //echo here_location;
            }
        }



        //Using ticketMaster
        include 'geoHash.php';
        $geoPoint = encode($lat, $lon);
        //echo "geoHash: "."$geoPoint";


        if (($_GET["distance"] == 'f')) {
            $radius = 10;
        }
        else {
            $radius = $_GET["distance"];
        }
        $segmentArray = array(
            "default" => "",
            "music" => "KZFzniwnSyZfZ7v7nJ",
            "sports" => "KZFzniwnSyZfZ7v7nE",
            "arts" => "KZFzniwnSyZfZ7v7na",
            "film" => "KZFzniwnSyZfZ7v7nn",
            "miscellaneous" => "KZFzniwnSyZfZ7v7n1"
        );
        $apiKey = 'ycieqKxDYckllhDLJLzg6ddfaaHOWAQ0';
        $segmentId = $segmentArray[$_GET["options"]];
        //echo "<br>".$radius." ".$segmentId;
        $keyword = $_GET["keyword"];
        $keyword = str_replace(' ', '+', $keyword);
        //echo "<br>".$keyword;
        if ($segmentId == '') {
            $urlTM = 'https://app.ticketmaster.com/discovery/v2/events.json?apikey='.$apiKey.'&keyword='.$keyword.'&radius='.$radius.'&unit=miles&geoPoint='.$geoPoint;
        }
        else {
            $urlTM = 'https://app.ticketmaster.com/discovery/v2/events.json?apikey='.$apiKey.'&keyword='.$keyword.'&segmentId='.$segmentId.'&radius='.$radius.'&unit=miles&geoPoint='.$geoPoint;
        }

        //echo "$urlTM";
        $retrieveEvents = file_get_contents("$urlTM");
        //echo "$retrieveEvents";
        //$jsonTM = json_decode($retrieveLoc, true);
        echo $retrieveEvents;
        //echo "#########################################################################################";

    }

        //Getting event details
    elseif(isset($_GET['id'])) {
            $eventId = $_GET['id'];
            $api_key = 'ycieqKxDYckllhDLJLzg6ddfaaHOWAQ0';
            $events_url = 'https://app.ticketmaster.com/discovery/v2/events/' .$eventId. '?apikey='.$api_key;
            //echo "$events_url";
            $retrieveIDEvents = file_get_contents($events_url);
            echo $retrieveIDEvents;
        }

    elseif(isset($_GET['venue'])) {
            $venue = $_GET['venue'];
            $venue = urlencode($venue);
            $api_key = 'ycieqKxDYckllhDLJLzg6ddfaaHOWAQ0';
            $venue_url = 'https://app.ticketmaster.com/discovery/v2/venues?apikey=' .$api_key. '&keyword=' .$venue;
            //echo "$venue_url";
            $venueEvents = file_get_contents($venue_url);
            echo $venueEvents;
        }

   else {
?>

<!DOCTYPE html>
<html>
<head>
<?php
    echo "<style>
     .events{
        margin: 0 auto;
        border-collapse: collapse;
        table-layout: fixed;
        width: 1000px;
        border: 1px solid #e3dcdc;
        margin-top: 8px;
    }

    .col1 {
        width: 300px;
        border: 1px solid #e3dcdc;
    }
    .col2 {
        font-style: normal;
        width: 700px;
        border: 1px solid #e3dcdc;
        font-weight: normal;
    }
    #walkVal:hover, #bikeVal:hover, #driveVal:hover{
        color:gray;
        background:#d8d1d1;
    }</style>"


?>
<title></title>

</head>
<body>

    <!-- Creating the Form -->
    <div id=form_parent align="center" style="margin: auto; width:750px; border: 3px solid #cac7c7; background-color:#faf9fa;  overflow:hidden; padding: 0px 0px 0px 0px; margin-top: 50px; margin-bottom: 50px; text-align:center">
    <FORM onsubmit="return false">
        <font size="6px" style="text-align:center; padding-left: 0px; padding-top:0px; margin-top: 0px; margin-bottom: 0px;"><i>Events Search</></font>
    	<TABLE BORDER="0px" bgcolor="#faf9fa" border="none" border-style="none" border-collapse="collapse" align="center" width="745px" height="200px" style="padding:0px 0px 0px 0px; margin-left:2px; margin-top:0px;">
            <tbody style="font-style:normal;">



    		<tr>
    			<td style=" border-top: 3px solid #cac7c7; text-align:left;"><b>Keyword</b>
                <INPUT type="TEXT" style="test-align:left; width: 150px" id="key" name="keyword" required>
                </td>
            </tr>
            <tr>
                <!-- TODO: check how to retain options value-->
                <td style="text-align:left;"><b>Category</b>
    			<SELECT name="options" id="select">
                    <option value="default">Default</option>
                    <option value="music">Music</option>
                    <option value="sports">Sports</option>
                    <option value="arts">Arts & Theatre</option>
                    <option value="film">Film</option>
                    <option value="miscellaneous">Miscellaneous</option>
                </td><br>
            </tr>
            <tr>
                <td style="text-align:left;"><b>Distance (miles)</b> <INPUT id="dist" type="TEXT" name="distance" style="width:150px;" placeholder="10">
                    <b>from</b> <INPUT type="radio" name="location"  id="here_input" checked>Here<br>
                    <INPUT type="radio" style="margin-left:52%" name="location" id="loc_radio" >
                    <INPUT type="TEXT" style="text-align:left; width: 150px" name="loc" id="loc_input" placeholder="location" align="right">
                </td>
    		</tr>
            <tr>
                <td style="padding-left:0px; padding-right:420px;"><INPUT type="submit" onclick="getParsetable()" id="search_field" value="Search" name="submit" disabled="true" >
                <INPUT type="RESET" value="Clear" name="reset" onclick=clearDoc()></td>
            </tr>
        </tbody>
    	</TABLE>

    </FORM>
    </div>
    <div id="displayDiv"></div>
    <div id="newDiv" style="position: absolute; height: 300px; width: 400px; display:none; "></div>

    <script src = "https://maps.googleapis.com/maps/api/js?key=AIzaSyDwF0qbAiBnIfYnzY7Rukw4tJHo23sIQIQ&callback"></script>
    <script >

        map = null;
        marker = null;

        //Getting latitude longitude from ip-api
        var xmlhttp=new XMLHttpRequest();
        xmlhttp.open("GET",'http://ip-api.com/json', false); //open, send, responseText are
        xmlhttp.send(); //properties of XMLHTTPRequest
        console.log(xmlhttp.responseText);
        jsonDoc = xmlhttp.responseText;
        var myObj = JSON.parse(jsonDoc);
        var lat = myObj["lat"]
        var lon = myObj["lon"]
        console.log(lat);
        console.log(lon);

        //Tooltip condns and setting Default Lat Lng values
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            document.getElementById('search_field').disabled = false;
        }

        if (document.getElementById('here_input').checked){
            document.getElementById('loc_input').disabled = true;
            document.getElementById('here_input').value = lat.toString() + "%23" + lon.toString();
        }

        document.getElementById('here_input').addEventListener('click', function() {

                document.getElementById('loc_input').disabled = true;
                document.getElementById('here_input').value = lat.toString() + "%23" + lon.toString();
                document.getElementById('loc_input').placeholder = "location";

      })


        document.getElementById("loc_radio").addEventListener('click', function(){
            document.getElementById('loc_input').disabled = false;
            document.getElementById("loc_input").required = this.checked ;
        })

        console.log("ALVE?");
        console.log(document.getElementById('key').value + "-" + document.getElementById("here_input").checked + "-" + document.getElementById('loc_input').value );




        function getParsetable() {

            console.log("cam to getparse");

            if (document.getElementById('key').value == '') {
                document.getElementById("displayDiv").innerHTML = "";
                return

            }
            if(!(document.getElementById("here_input").checked) && document.getElementById('loc_input').value == ''){
                document.getElementById("displayDiv").innerHTML = "";
                return
            }


            var xmlhttp=new XMLHttpRequest();
            var key = document.getElementById('key').value;
            if (key == '') {
                key = "f";
            }
            var select = document.getElementById('select').value;
            var dist = document.getElementById('dist').value;
            if (dist == '') {
                dist = "f";
            }
            var here_input = document.getElementById('here_input').value;
            var loc_input = document.getElementById('loc_input').value;
            if (loc_input == ''){
                loc_input = "f";
            }


            console.log("loc_input: " + loc_input);


            var urll = 'http://localhost/sample.php?initalTable='+ 1 + '&keyword='+ key + '&options='+ select + '&distance='+ dist + '&location=' + here_input + '&loc=' + loc_input;

            console.log(urll);
            xmlhttp.open("GET",urll , false);
            xmlhttp.send(); //properties of XMLHTTPRequest
            console.log(xmlhttp.responseText);
            var jsonTable= JSON.parse(xmlhttp.responseText);
            console.log(jsonTable);
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

                parsetoTable(jsonTable);
            }

        }


        function parsetoTable(jsonTM) {


            var tbl = '<table border="1" align="center" style="width:1200px; border: 5px lightgrey; border-collapse: collapse;">';

            var tdata = jsonTM['_embedded'];

            console.log(tdata);

            if (tdata == undefined) {
                // if (!document.getElementById("key") == 'f'){
                //     console.log("here");
                tbl += '<table align="center"><tr><td style="text-align: center; background: #e7e7e7; border: 2px solid #bcbbbb; width: 900px; font-style:normal;">No Records has been Found</td></tr></table>';
                //}

            }

            else  {

                tbl += '<tr><th style="font-style:normal;">Date</th><th style="font-style:normal;">Icon</th><th style="font-style:normal;">Event</th><th style="font-style:normal;">Genre</th><th style="font-style:normal;">Venue</th></tr>';
                console.log(tdata['events'].length)
                for (var i=0; i< tdata['events'].length; i++) {
                    tbl += '<tr>';

                    tbl += '<td style="width:80px; padding: 10px; align=center; font-style:normal;">';
                    try{
                        tbl += tdata['events'][i]['dates']['start']['localDate'];
                        tbl+='<br>';
                        try {
                            tbl += tdata['events'][i]['dates']['start']['localTime'];
                            tbl += '</td>';
                        }
                        catch{

                        }

                    }
                    catch{
                        tbl += 'N / A';
                        tbl += '</td>';
                    }


                    tbl += '<td style="width:150px; padding: 10px;" align=center; font-style:normal;>';
                    try{
                        tbl += '<img src="' + tdata['events'][i]['images'][0]['url'] + '"style="width:100px;height:75px">'
                        tbl += '</td>';
                    }
                    catch {
                        tbl += 'N / A';
                        tbl += '</td>';

                    }

                    //DIDNT CHECK ANY CONDN FOR VENUE NAME
                    var eventsname = tdata['events'][i]['id'];
                    var venuename = tdata['events'][i]['_embedded']['venues'][0]['name'];
                    venuename = venuename.replace(/ /g,"%20");
                    name = eventsname + "###" + venuename
                    console.log("venuename: " + venuename);
                    tbl += '<td style="padding: 15px; font-style:normal;"><a onmouseover="changeColor(this)" onmouseout="changeColorback(this)" href="#" style="text-decoration: none; color:black; " class="venueLink"  onclick=getEventsJSON("'+name+'")>';
                    tbl += tdata['events'][i]['name'];
                    tbl += '</a></td>';

                    tbl += '<td style="width:80px; padding: 15px; font-style:normal;">';
                    try {
                        (tdata['events'][i]['classifications'][0]['segment']['name']);
                        tbl += tdata['events'][i]['classifications'][0]['segment']['name'];
                    }
                    catch {
                        tbl += 'N / A';
                    }

                    tbl += '</td>';


                    var destlat = tdata['events'][i]['_embedded']['venues'][0]['location']['latitude'];
                    var destlon = tdata['events'][i]['_embedded']['venues'][0]['location']['longitude'];
                    tbl += '<td onmouseover="changeColor(this)" onmouseout="changeColorback(this)" id="venue' + i + '"style="padding: 15px; font-style:normal;" onclick=getnewDiv(this' + ',' + lat + ','+ lon + "," + destlat + ',' + destlon + ')>';
                    tbl += tdata['events'][i]['_embedded']['venues'][0]['name'];
                    tbl += '</td>';

                    tbl += '</tr>';

                }

                tbl += '</table>';
            }
            document.getElementById("displayDiv").innerHTML = tbl;

        }

        function getEventsJSON(idvenue){
            id = idvenue.split("###")[0];
            venuename = idvenue.split("###")[1];
            console.log(id);
            console.log(venuename);
            var xmlhttp=new XMLHttpRequest();
            xmlhttp.open("GET",'http://localhost/sample.php?id='+id, false); //open, send, responseText are
            xmlhttp.send(); //properties of XMLHTTPRequest
            console.log(xmlhttp.responseText);
            var jsonEvents= JSON.parse(xmlhttp.responseText);

            var xmlhttp=new XMLHttpRequest();
            xmlhttp.open("GET",'http://localhost/sample.php?venue='+venuename, false); //open, send, responseText are
            xmlhttp.send(); //properties of XMLHTTPRequest
            console.log(xmlhttp.responseText);
            var jsonVenue = JSON.parse(xmlhttp.responseText);

            makeEventTable(jsonEvents, jsonVenue);



        }

        function changeColor(obj){
            obj.style.color = "gray";
        }
        function changeColorback(obj) {
            obj.style.color = "black";
        }


        function makeEventTable(jsonEventDetails, venueDetails) {

            console.log("makeEventTable");
            console.log(jsonEventDetails['name']);
            var eventDetails = '';
            eventDetails += '<h2 align="center" style="font-style: normal;">' + jsonEventDetails['name'] + '<\h3>'

            try {
                var x = jsonEventDetails['seatmap']['staticUrl'];
                eventDetails += '<div style="float:right; margin-right:290px;">';
                eventDetails += '<img height="300px" width="550px" src="' + jsonEventDetails['seatmap']['staticUrl'] + '"/>';
                eventDetails += '</div>';

                eventDetails += '<div style="float:left; margin-left:420px;" id="venueTable">'
                eventDetails += '<table><tbody style="font-style: normal;">';
                console.log("float left");
            }
            catch {
                eventDetails += '<div style="float:left; margin-left:845px;" id="venueTable">'
                eventDetails += '<table><tbody style="font-style: normal;">';
                console.log("float left>>>");
            }

            try {
                var y = jsonEventDetails['dates']['start']['localDate'];
                var z = jsonEventDetails['dates']['start']['localTime'];
                console.log("doing date");
                eventDetails += '<tr><th align="left">Date</th></tr>';
                eventDetails += '<tr><td style="font-weight:normal;">';
                eventDetails += jsonEventDetails['dates']['start']['localDate'] + ' ' + jsonEventDetails['dates']['start']['localTime'];
                eventDetails += '</td</tr>';

            }
            catch {

            }



            try {
                console.log("attractions");
                for (var i=0; i< jsonEventDetails['_embedded']['attractions'].length; i++) {
                    if (i == 0){
                        eventDetails += '<tr><th align="left">Artist/Team</th></tr>';
                        eventDetails += '<tr><td style="font-weight:normal;">';
                    }

                    try {
                        var y = jsonEventDetails['_embedded']['attractions'][i]['url'];
                        eventDetails += '<a onmouseover="changeColor(this)" onmouseout="changeColorback(this)" style="text-decoration: none; color:black; font-weight:normal; font-style:normal;" href="' + jsonEventDetails['_embedded']['attractions'][i]['url'] + '" target="_blank">';
                        if (i==0){
                            eventDetails += jsonEventDetails['_embedded']['attractions'][i]['name'] + '</a>';
                        }
                        else {
                            eventDetails += " | " + jsonEventDetails['_embedded']['attractions'][i]['name'] + '</a>';
                        }
                    }
                    catch {
                        eventDetails += jsonEventDetails['_embedded']['attractions'][i]['name'];
                    }


                }
                eventDetails += '</td></tr>';
            }
            catch {

            }

            try{
                var x = jsonEventDetails['_embedded']['venues'][0]['name'];
                eventDetails += '<tr><th align="left">Venue</th></tr>';
                eventDetails += '<tr><td style="font-weight:normal;">';
                eventDetails += jsonEventDetails['_embedded']['venues'][0]['name'];
                eventDetails += '</td></tr>';
            }
            catch{

            }



            console.log(jsonEventDetails['classifications']);

            if (jsonEventDetails['classifications'] != undefined) {
                eventDetails += '<tr><th align="left">Genre</th></tr>';
                eventDetails += '<tr><td style="font-weight:normal;">';

                if (jsonEventDetails['classifications'][0]['subGenre']['name'] != "Undefined"){
                    eventDetails += jsonEventDetails['classifications'][0]['subGenre']['name'];
                }
                if (jsonEventDetails['classifications'][0]['genre']['name'] != "Undefined") {
                    eventDetails += ' | '+ jsonEventDetails['classifications'][0]['genre']['name'] ;
                }
                if (jsonEventDetails['classifications'][0]['segment']['name'] != "Undefined") {
                    eventDetails += ' | ' + jsonEventDetails['classifications'][0]['segment']['name'];
                }
                if (jsonEventDetails['classifications'][0]['subType']['name'] != "Undefined") {
                    eventDetails += ' | ' + jsonEventDetails['classifications'][0]['subType']['name'];
                }
                if (jsonEventDetails['classifications'][0]['type']['name'] != "Undefined") {
                    eventDetails += ' | ' + jsonEventDetails['classifications'][0]['type']['name'];
                }


                eventDetails += '</td></tr>';
            }
            else {

            }

            try {
                var x = jsonEventDetails['priceRanges'][0];
                eventDetails += '<tr><th align="left">Price Ranges</th></tr>';
                eventDetails += '<tr><td style="font-weight:normal;">';
                try{
                    eventDetails += jsonEventDetails['priceRanges'][0]['min'] ;
                    try {
                        eventDetails += ' - ' + jsonEventDetails['priceRanges'][0]['max'];
                    }
                    catch {

                    }
                }
                catch{

                }
                try {
                    eventDetails += ' ' + jsonEventDetails['priceRanges'][0]['currency'];
                }
                catch {

                }

                eventDetails += '</tr></td>';

            }
            catch {

            }


            try {
                var x = jsonEventDetails['dates']['status']['code'];
                eventDetails += '<tr><th align="left">Ticket Status</th></tr>';
                eventDetails += '<tr><td style="font-weight:normal;">';
                eventDetails += jsonEventDetails['dates']['status']['code'];
                eventDetails += '</td></tr>';
            }
            catch {

            }


            eventDetails += '<tr><th align="left">Buy Ticket At</th></tr>';
            eventDetails += '<tr><td style="font-weight:normal;">';
            try{
                eventDetails += '<a onmouseover="changeColor(this)" onmouseout="changeColorback(this)" style="text-decoration: none; color:black; " href="' + jsonEventDetails['url'] + '" target="_blank">';
            }
            catch{

            }
            eventDetails += 'Ticketmaster';
            eventDetails += '</a></td></tr>';


            eventDetails += '</tbdody></table>';
            eventDetails += '</div>';




            eventDetails += '<br><br>';
            eventDetails += '<div style="clear: left; width: 50%; margin: 0px auto; text-align: center;">';
            eventDetails += '<br><br>';
            eventDetails += '<p style="font-size:12px; color:gray;">click to show venue info</p>';
            //TODO: onclick need to display content and hide content accordingly


            // eventDetails += '<script src=' + '"https://maps.googleapis.com/maps/api/js?key=AIzaSyDwF0qbAiBnIfYnzY7Rukw4tJHo23sIQIQ"' + '>';
            var latitude = venueDetails['_embedded']['venues'][0]['location']['latitude'];
            var longitude = venueDetails['_embedded']['venues'][0]['location']['longitude'];


            //origlat, origlong, destlat, destlon, selectedMode, flag
            eventDetails += '<img id="infoImg" height="20px" width="25px" src="http://csci571.com/hw/hw6/images/arrow_down.png" onclick="displayInfo(venueInfo, infoImg);initMap(' + latitude + ',' +longitude + ',' + 'false' +',' +'false' +',' + 'false' + ',' + 'false' +')"/></a><br>';


            eventDetails += '<div id="venueInfo" style="display:none;">';
            eventDetails += '<table class="events">';
            eventDetails += '<tbody style="font-style:normal;">';
            //console.log(venueDetails['_embedded']['venues'][0]['name']);
            try {
                var x = venueDetails['_embedded']['venues'][0]['name'];
                eventDetails += '<tr><td class="col1" align="right" ><b>Name</b></td>';
                eventDetails += '<td class="col2" align="center">' + venueDetails['_embedded']['venues'][0]['name'] + '</td></tr>';

                eventDetails += '<tr><td class="col1" align="right" ><b>Map</b></td>';

                eventDetails += '<td class="col2">' + '<div float="left" style="background:#efefef; width: 80px; height: 70px; margin-left: 20px; margin-top: 60px; position: absolute; "><button id="walkVal" value="false" onclick="renderDirns(this,'+ lat +','+ lon +',' + latitude + ','+ longitude+')" style="padding: 0px;border: none; background: #efefef;">Walk there</button><br><button id="bikeVal" value="false" onclick="renderDirns(this,'+ lat +','+ lon +',' + latitude + ','+ longitude+')" style="padding: 0px; border: none; background: #efefef;">Bike there</button><br><button id="driveVal" value="false" onclick="renderDirns(this,'+ lat +','+ lon +',' + latitude + ','+ longitude+')" style="padding: 0px; border: none; background: #efefef;">Drive there</button></div><div float="right" id="map" style = "width:500px; height:400px; margin-left: 122px; margin-top:10px; margin-bottom:10px;"></div></td></tr>';

                eventDetails += '<tr><td class="col1" align="right"><b>Address</b></td>';
                eventDetails += '<td class="col2" align="center">' + venueDetails['_embedded']['venues'][0]['address']['line1'] + '</td></tr>';

                eventDetails += '<tr><td class="col1" align="right"><b>City</b></td>';
                eventDetails += '<td class="col2" align="center">' + venueDetails['_embedded']['venues'][0]['city']['name'] + ', ';
                eventDetails +=   venueDetails['_embedded']['venues'][0]['state']['stateCode'] + '</td></tr>';

                eventDetails += '<tr><td class="col1" align="right"><b>Postal Code</b></td>';
                eventDetails += '<td class="col2" align="center">' + venueDetails['_embedded']['venues'][0]['postalCode'] + '</td></tr>';

                eventDetails += '<tr><td class="col1" align="right" ><b>Upcoming Events</b></td>';
                eventDetails += '<td class="col2" align="center"><a onmouseover="changeColor(this)" onmouseout="changeColorback(this)" style="text-decoration: none; color:black;" href="' + venueDetails['_embedded']['venues'][0]['url'] + '" target="_blank">'+ venueDetails['_embedded']['venues'][0]['name'] + 'Tickets' + '</a></td></tr>';


                eventDetails += '</tbody>'
                eventDetails += '</table>';
                eventDetails += '</div>'


                eventDetails += '<p style="font-size:12px; color:gray;">click to show venue photos</p>'
                eventDetails += '<img id="photosImg" height="20px" width="25px" src="http://csci571.com/hw/hw6/images/arrow_down.png" onclick="displayInfo(venuePhotos, photosImg);"/>'
                eventDetails += '<div id="venuePhotos" style="display:none;">'
                eventDetails += '<table class="events">';


                try {
                    if (venueDetails['_embedded']['venues'][0]['images'].length == 0){
                        eventDetails += '<tr><td><b>No Venue Photos Found</b></td></tr>';
                    }
                    else {
                        for (var i=0; i< venueDetails['_embedded']['venues'][0]['images'].length; i++){
                            eventDetails += '<tr style="border:1px solid #e3dcdc;"><div width="800"><td align="center"><img style="max-width: 85%" src="' + venueDetails['_embedded']['venues'][0]['images'][i]['url'] + '"/></td></div></tr>';

                        }
                    }

                }
                catch {
                    eventDetails += '<tr><td style="font-style:normal;"><b>No Venue Photos Found</b></td></tr>';
                }
            }
            catch {
                eventDetails += '<tr><td style="font-style:normal;"><b>No Venue Info Found</b></td></tr>';
            }


            eventDetails += '</table>';
            eventDetails += '</div>'


            document.getElementById("displayDiv").innerHTML = eventDetails;



        }

        function changeButtonColor(id){
            document.getElementById(id).style.color = "gray";
            document.getElementById(id).style.background = "#d8d1d1";

        }
        function changeButtonBack(id){
            document.getElementById(id).style.color = "black";
            document.getElementById(id).style.background = "#efefef";

        }



        var displayedMap = false;
        var directionsDisplay = new google.maps.DirectionsRenderer;
        var directionsService = new google.maps.DirectionsService;
        var walk = "WALKING";
        var bike = "BICYCLING";
        var drive = "DRIVING";
        function initMap(origlat, origlong, destlat, destlon, selectedMode, flag) {

            console.log("initmpa");
            console.log(parseFloat(origlat));
            console.log(parseFloat(origlong));
            console.log(parseFloat(destlat));
            console.log(parseFloat(destlon));
            console.log(selectedMode);
            console.log(typeof 'false' + " " + typeof flag);

            //console.log(parseFloat(origlong));


            console.log("came in");
            var uluru = {lat: parseFloat(origlat), lng: parseFloat(origlong)};
            // The map, centered at Uluru
            map = new google.maps.Map(
            document.getElementById('map'), {zoom: 6, center: uluru});
            // The marker, positioned at Uluru

            marker = new google.maps.Marker({position: uluru, map: map});

            // var directionsDisplay = new google.maps.DirectionsRenderer;
            // var directionsService = new google.maps.DirectionsService;
            directionsDisplay = new google.maps.DirectionsRenderer({
                map: map
              });

            if (flag == 'true'){

                calculateAndDisplayRoute(selectedMode, origlat, origlong, destlat, destlon);
            }

        }



    function getnewDiv(tdObj, originlat, originlon, destlat, destlon){
            console.log("get new div: ")
            console.log(originlat);
            console.log(originlon);
            console.log(destlat);
            console.log(destlon);
            if (displayedMap) {
                document.getElementById("newDiv").style.display = "none";
                displayedMap = false;
            }
            else {
                var tdId = tdObj.id;
                console.log(tdId);
                var elem = document.getElementById(tdId);
                var coord = elem.getBoundingClientRect();
                var divElement = document.getElementById("newDiv");
                divElement.style.left = coord.left+ 45 + "px";
                divElement.style.top = (coord.top + 60) + "px";
                displayedMap =true;
                document.getElementById("newDiv").style.display = "block";

                var uluru = {lat: parseFloat(originlat), lng: parseFloat(originlon)};
                // The map, centered at Uluru
                map = new google.maps.Map(
                document.getElementById('newDiv'), {zoom: 6, center: uluru});
                // The marker, positioned at Uluru

                marker = new google.maps.Marker({position: uluru, map: map});

                // var directionsDisplay = new google.maps.DirectionsRenderer;
                // var directionsService = new google.maps.DirectionsService;
                directionsDisplay = new google.maps.DirectionsRenderer({
                    map: map
                  });

                var buttonDiv = document.createElement('div');
                var walk = "WALKING";
                var bike = "BICYCLING";
                var drive = "DRIVING";

                buttonDiv.innerHTML = "<div style='padding:5px;font-style:normal;' id='walkVal' onclick=calculateAndDisplayRoute('"+walk+"'," + originlat + "," + originlon + "," +destlat + ","+ destlon+")> Walk there</div><div style='padding:5px;font-style:normal;' id='bikeVal' onclick=calculateAndDisplayRoute('"+bike+"'," + originlat + "," + originlon + "," +destlat + ","+ destlon+")> Bike there</div><div style='padding:5px;font-style:normal;' id='driveVal' onclick=calculateAndDisplayRoute('"+drive+"'," + originlat + "," + originlon + "," +destlat + ","+ destlon+")> Drive there</div>";
                buttonDiv.style.border = "1px solid #efefef";
				buttonDiv.style.background = "#efefef";
				buttonDiv.style.position = "absolute";
                buttonDiv.setAttribute("font-style" , "normal");

				divElement.appendChild(buttonDiv);

            }



        }

        function calculateAndDisplayRoute(selectedMode, originlat, originlon, destlat, destlon) {
            marker.setMap(null);

            console.log(selectedMode);
            console.log("Calcroute");
            console.log(originlat);
            console.log(originlon);
            directionsService.route({
              origin: {lat: parseFloat(originlat), lng: parseFloat(originlon)},  // Haight.
              destination: {lat: parseFloat(destlat), lng: parseFloat(destlon)},  // Ocean Beach.
              // Note that Javascript allows us to access the constant
              // using square brackets and a string value as its
              // "property."
              travelMode: google.maps.TravelMode[selectedMode]
            }, function(response, status) {
              if (status == 'OK') {
                directionsDisplay.setDirections(response);
              } else {
                window.alert('Directions request failed due to ' + status);
              }
            });

        }

        function renderDirns(btn, lat, lon, destlat, destlon) {

            console.log("finding directions");

            if (btn.id == "walkVal"){
                btn.value = true;
                document.getElementById("bikeVal").value = false;
                document.getElementById("driveVal").value = false;
                selectedMode = "WALKING";
                initMap(lat, lon, destlat, destlon, selectedMode, 'true' );

            }
            else if (btn.id == "bikeVal") {
                btn.value = true;
                document.getElementById("walkVal").value = false;
                document.getElementById("driveVal").value = false;
                selectedMode = "BICYCLING"
                initMap(lat, lon, destlat, destlon, selectedMode, 'true' );
            }
            else if (btn.id == "driveVal") {
                btn.value = true;
                document.getElementById("bikeVal").value = false;
                document.getElementById("walkVal").value = false;
                selectedMode = "DRIVING";
                initMap(lat, lon, destlat, destlon, selectedMode,'true' );
            }


        };

        function displayInfo(id,imgId) {
            var x = id;
            var y = imgId;
            if (x.style.display === "none") {
                y.src = "http://csci571.com/hw/hw6/images/arrow_up.png";
                x.style.display = "block";
            } else {
                y.src = "http://csci571.com/hw/hw6/images/arrow_down.png";
                x.style.display = "none";
            }

        }

        function clearDoc(){
            var dis = document.getElementById("displayDiv");
            dis.innerHTML="";
            document.getElementById("key").value = "";
            document.getElementById("select").value = "default";
            //document.getElementById("dist").value = " ";
            document.getElementById("dist").placeholder = 10;
            document.getElementById("here_input").checked = true;
            //document.getElementById("loc_input").value = "";
            document.getElementById("loc_input").placeholder = "location";

        }


    </script>

</body>
</html>
<?php } ?>
