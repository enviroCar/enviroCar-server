#!/bin/zsh -e

#URL=http://giv-car.uni-muenster.de:8080/dev
URL=http://localhost:8080/webapp

USERS=5
GROUPS=5
TRACKS=2
MEASUREMENTS=20
SENSORS=5

# users
for i in {1..$USERS}; do 
	curl -X POST \
		-H "Content-Type: application/json" \
		$URL/rest/users -d \
	"{
		\"name\": \"testuser$i\",
		\"mail\": \"testuser$i@example.org\",
		\"token\": \"testuser$i\"
	}"
done

# friends
for i in {1..$USERS}; do
	for j in {1..$USERS}; do
		curl -X POST \
			-H "Content-Type: application/json" \
			-H "X-Token: testuser$i" \
			-H "X-User: testuser$i" \
			$URL/rest/users/testuser$i/friends -d \
			"{ \"name\": \"testuser$j\" }"
	done
done

# groups
for i in {1..$GROUPS}; do
	curl -X POST \
		-H "Content-Type: application/json" \
		-H "X-Token: testuser$i" \
		-H "X-User: testuser$i" \
		$URL/rest/groups -d \
	"{
		\"name\": \"testgroup$i\",
		\"description\": \"this is the testgroup $i\"
	}"
done

# group memberships
for user in {1..$USERS}; do 
	for group in {1..$GROUPS}; do
		curl -X POST \
			-H "Content-Type: application/json" \
			-H "X-Token: testuser$user" \
			-H "X-User: testuser$user" \
			$URL/rest/groups/testgroup$group/members -d \
		"{ \"name\": \"testuser$user\" }"
	done
done

# sensors
for user in {1..$SENSORS}; do
	curl -X POST -H "Content-Type: application/json" \
		-H "X-Token: testuser$user" \
		-H "X-User: testuser$user" \
		$URL/rest/sensors -d \
	"{ \"name\": \"testsensor$user\" }"
done

# phenomenons
for user in {1..5}; do
	curl -X POST -H "Content-Type: application/json" \
		-H "X-Token: testuser$user" \
		-H "X-User: testuser$user" \
		$URL/rest/phenomenons -d \
	"{
	   \"name\": \"testphenomenon$user\",
	   \"unit\": \"testunit$user\"
	}"
done

# tracks
for user in {1..$USERS}; do 
	for track in {1..$TRACKS}; do
		curl -X POST \
			-H "Content-Type: application/json" \
			-H "X-Token: testuser$user" \
			-H "X-User: testuser$user" \
			$URL/rest/users/testuser$user/tracks -d \
		"{
			\"type\": \"FeatureCollection\",
			\"properties\": {
				\"name\": \"Track #$track of testuser$user\",
				\"description\": \"Track #$track of testuser$user\",
				\"sensor\": \"testsensor$(((($i - 1) % $SENSORS) + 1))\"
			}
		}"
	done
done


# measurements
for user in {1..$USERS}; do 
	curl -s $URL/rest/users/testuser$user/tracks \
		| python -m json.tool | grep href | cut -d '"' -f4 \
		| while read track; do

		for m in {1..$MEASUREMENTS}; do
			t=$(date  --iso-8601=seconds --date "-1 hour +$m minutes")
			curl -X POST -H "Content-Type: application/json" \
				-H "X-Token: testuser$user" \
				-H "X-User: testuser$user" \
				$track/measurements -d \
				"{
					\"type\": \"Feature\",
					\"geometry\": {
						\"type\": \"Point\", 
						\"coordinates\": [ $(($RANDOM / 1000.0)), $(($RANDOM / 1000.0)) ]
					},
					\"properties\": {
						\"time\": \"$t\",
						\"sensor\": { \"name\": \"testsensor$(((($user - 1) % $SENSORS) + 1))\" },
						\"phenomenons\": {
							\"testphenomenon1\": { \"value\": $(($RANDOM / 1000.0)) },
							\"testphenomenon2\": { \"value\": $(($RANDOM / 1000.0)) },
							\"testphenomenon3\": { \"value\": $(($RANDOM / 1000.0)) },
							\"testphenomenon4\": { \"value\": $(($RANDOM / 1000.0)) },
							\"testphenomenon5\": { \"value\": $(($RANDOM / 1000.0)) }
						}
					}
				}"
		done
	done &
done
