# DEPLOY

#cd ~/workspace/route-service-broker-thumbnail
#cf push

cd ~/workspace/gateway-s1p-2018/cool-app
cf push

cd ~/workspace/gateway-s1p-2018/route-service-broker
./gradlew assemble
cf cs mlab sandbox route-service-mongodb
cf cs rediscloud 30mb route-service-redis
cf push

# RECONFIGURE

cf create-service-broker techfest-route-service admin supersecret https://techfest-route-service-broker.cfapps.io --space-scoped

cf cs techfest-route-service standard subscription-gateway

cf map-route cool-app cfapps.io --hostname techfest-cool-app
cf brs cfapps.io subscription-gateway --hostname techfest-cool-app

cf map-route colorgateway cfapps.io --hostname techfest-colorgateway
cf brs cfapps.io subscription-gateway --hostname techfest-colorgateway