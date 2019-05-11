// We’ll declare all our dependencies here
const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const request = require('request');
var SpotifyWebApi = require('spotify-web-api-node');

//Initialize our app variable
const app = express();

app.use(cors());

//Middleware for bodyparsing using both json and urlencoding
app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json());



app.get('/', (req,res) => {
	res.sendFile('hw7form.html',{ root : __dirname});
})



app.get('/suggest', (req,res) => {
	var keyword = req.query.keyword;
	keyword = keyword.replace(/ /g,"%20");
	var apikey = req.query.apikey;
	console.log("keyword: " +  keyword);
	// res.send(keyword);
    // res.send("Sending from Node");

	var url = 'https://app.ticketmaster.com/discovery/v2/suggest?apikey=' + apikey + '&keyword='+ keyword;
	console.log(url);
	request.get(url, { json: true }, (err, response, body) => {
		if (err) { return console.log(err); }
		//console.log(JSON.stringify(body));
		//console.log(body.explanation);
		res.send(JSON.stringify(body));
	});


})


app.get('/events', (req,res) => {
	console.log("Entered Events");
	var keyword = req.query.keyword;
	keyword = keyword.replace(/ /g,"%20");
	var radius = req.query.radius;
	var unit = req.query.unit;
	unit = unit.toLowerCase();
	var locInput = req.query.locInput;
	var lat = req.query.lat;
	var lon = req.query.lon;
	var otherlocChecked = req.query.otherlocChecked;
	var segmentId = req.query.segmentId;

	if(otherlocChecked == "true"){
		var apikey = 'AIzaSyDwF0qbAiBnIfYnzY7Rukw4tJHo23sIQIQ&callback';
		var address = locInput.replace(/ /g,"%20");
		var url = 'https://maps.googleapis.com/maps/api/geocode/json?address=' + address + '&key=' + apikey;
		console.log(url);
		request.get(url, { json: true }, (err, response, body) => {
			if (err) { return console.log(err); }

			var lat = body.results[0].geometry.location.lat;
			var lon = body.results[0].geometry.location.lng;
			console.log(JSON.stringify(body));
			console.log(lat);
			console.log(lon);
			//check error condition
			var initializePromise = getEvents(keyword, radius, unit, lat, lon, segmentId);
		    initializePromise.then(function(result) {
				console.log("result: ");
				console.log(result);
				res.send(result);
			}, function(err) {
				console.log(err);
			})

		})
	}
	else{
		//check error condition
		var initializePromise = getEvents(keyword, radius, unit, lat, lon, segmentId);
		initializePromise.then(function(result) {
			console.log("result: ");
			console.log(result);
			res.send(result);
		}, function(err) {
			console.log(err);
		})
	}
})

function getEvents(keyword, radius, unit, lat, lon, segmentId){
	console.log("Entered getEvents");
	var geohash = require('ngeohash');
	var geoPoint = geohash.encode(lat, lon);
	console.log("geoPoint");
	console.log(geoPoint);
	var url_val = 'https://app.ticketmaster.com/discovery/v2/events.json?apikey=ycieqKxDYckllhDLJLzg6ddfaaHOWAQ0' + '&keyword=' + keyword + '&segmentId='+ segmentId +'&radius=' + radius + '&unit=' + unit + '&geoPoint=' + geoPoint + '&sort=date,asc';
	console.log(url_val);
	return new Promise((resolve, reject) => {
        request({url: url_val}, (err, res, body) => {
            if(err){
                reject(err);
            }
            else{
				console.log("answer:");
				console.log(JSON.parse(body));
                resolve(JSON.parse(body));
            }
        });
    });

}

app.get('/eventDetails', (req,res) => {
	var apikey = 'ycieqKxDYckllhDLJLzg6ddfaaHOWAQ0';
	var id = req.query.id;
	var url = 'https://app.ticketmaster.com/discovery/v2/events/' + id + '?apikey=' + apikey;
	console.log(url);
	request.get(url, { json: true }, (err, response, body) => {
		if (err) { return console.log(err); }
		console.log("event dets: ")
		console.log(JSON.stringify(body));
		//console.log(body.explanation);
		res.send(JSON.stringify(body));
	});
})


const spotifyApi = new SpotifyWebApi({
    clientId: 'a1515592a8674a82861e6bcea266b294',
    clientSecret: '5fbfe7b6ee7d495486aea2fb6acbb991'
});
app.get('/spotify', (req, res) => {
    let artist_name = req.query.artist;
    spotifyApi.searchArtists(artist_name).then((data) => {
        let items = data.body.artists.items;
        for(let item of items)
        {
            if(item.name.toUpperCase() == artist_name.toUpperCase())
            {
                res.send(item)
            }
        }
        res.send(items);
    }, (err) => {
        if(err.statusCode == 401)
        {
            spotifyApi.clientCredentialsGrant().then(
                function(data) {
                    spotifyApi.setAccessToken(data.body['access_token']);
                    spotifyApi.searchArtists(artist_name).then((data) => {
                        let items = data.body.artists.items;
                        for(let item of items)
                        {
                            if(item.name.toUpperCase() == artist_name.toUpperCase())
                            {
                                res.send(item)
                            }
                        }
                        res.send("");
                    }, (err) => {
                        console.log('Something went wrong when retrieving an access token1', err);
                    });
                },
                function(err) {
                    console.log('Something went wrong when retrieving an access token', err);
                }
            );
        }
    });
});


app.get('/googlePhotos', (req,res) => {
	console.log("in");
	var apikey = 'AIzaSyD9CX5zqluQig3fwBC1ZAWkmDuKCRYFCBM';
	var searchExp = req.query.searchExp;
	searchExp = searchExp.replace(/ /g,"%20");
	var id = '001594595413284253437:purkflisxq8';
	console.log(id);
	var url = 'https://www.googleapis.com/customsearch/v1?q='+ searchExp +'&cx='+ id +'&imgSize=huge&imgType=news&num=9&searchType=image&key=' + apikey;
	console.log(url);
	request.get(url, { json: true }, (err, response, body) => {
		if (err) { return console.log(err); }
		console.log("googlePhotos dets: ")
		console.log(JSON.stringify(body));
		res.send(JSON.stringify(body));
	});
})

app.get('/venueDetails', (req,res) => {
	var apikey = 'ycieqKxDYckllhDLJLzg6ddfaaHOWAQ0';
	var keyword = req.query.name;
	keyword = keyword.replace(/ /g,"%20");
	var url = 'https://app.ticketmaster.com/discovery/v2/venues.json?keyword=' + keyword + '&apikey=' + apikey;
	console.log(url);
	request.get(url, { json: true }, (err, response, body) => {
		if (err) { return console.log(err); }
		console.log("venue dets: ")
		var jsonObj = {};
		if(body._embedded.venues[0].name){
			jsonObj['name'] = body._embedded.venues[0].name;
		}
		if(body._embedded.venues[0].address.line1){
			jsonObj['address'] = body._embedded.venues[0].address.line1;
		}
		if(body._embedded.venues[0].city.name && body._embedded.venues[0].state.name){
			jsonObj['city'] = body._embedded.venues[0].city.name + ", " + body._embedded.venues[0].state.name;
		}
		if(body._embedded.venues[0].boxOfficeInfo.phoneNumberDetail){
			jsonObj['phone'] = body._embedded.venues[0].boxOfficeInfo.phoneNumberDetail;
		}
		if(body._embedded.venues[0].boxOfficeInfo.openHoursDetail){
			jsonObj['openHrs'] = body._embedded.venues[0].boxOfficeInfo.openHoursDetail;
		}
		if(body._embedded.venues[0].generalInfo.generalRule){
			jsonObj['generalRule'] = body._embedded.venues[0].generalInfo.generalRule;
		}
		if(body._embedded.venues[0].generalInfo.childRule){
			jsonObj['childRule'] = body._embedded.venues[0].generalInfo.childRule;
		}


		res.send(JSON.stringify(body));
	});
})

app.get('/songkickVenueID', (req,res) => {
	var apikey = 'ycieqKxDYckllhDLJLzg6ddfaaHOWAQ0';
	var venueName = req.query.name;
	venueName = venueName.replace(/ /g,"%20");
	var url = 'https://api.songkick.com/api/3.0/search/venues.json?query=' +venueName +'&apikey=' +'MVXHLPpRjhPAs2g5';
	console.log("SongKickID");
	console.log(url);
	request.get(url, { json: true }, (err, response, body) => {
		if (err) { return console.log(err); }
		//console.log(JSON.stringify(body));
		var jsonObj = {};
		jsonObj['id'] = body.resultsPage.results.venue[0].id;
		console.log(jsonObj['id']);
		res.send(JSON.stringify(jsonObj));
	});
})

app.get('/songkickVenueDetails', (req,res) => {
	var apikey = 'MVXHLPpRjhPAs2g5';
	var id = req.query.id;
	var url = 'https://api.songkick.com/api/3.0/venues/'+id +'/calendar.json?apikey='+ apikey;
	console.log("SongKickID");
	console.log(url);
	request.get(url, { json: true }, (err, response, body) => {
		if (err) { return console.log(err); }
		//console.log(JSON.stringify(body));


		res.send(JSON.stringify(body));
	});
})

var port = process.env.PORT || 1337;
app.listen(port);

console.log("Server running at http://localhost:%d", port);
