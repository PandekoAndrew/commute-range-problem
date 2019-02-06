FROM mongo

COPY ./init.json /init.json
CMD mongoimport --host mongodb --db crp --collection roads --type json --file /init.json --jsonArray