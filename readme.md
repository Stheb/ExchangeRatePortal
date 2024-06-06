# Start-up
Start ExchangeRatePortalApplication - will be hosted default http://localhost:8080 \
Start ExchangeRateFE - will be hosted default http://localhost:4200

# DB
Database can be accessed from http://localhost:8080/h2-console/ \
with **NO** password and username capybara

# Application
Works fully on async database querying using r2dbc and reactive webclient to make async calls. \
On startup it queries from LbLt today's exchange rate and currencies to the db. Makes calls every 24 hours to receive the latest updates.

On the website it shows daily result of exchange rates. Dropdowns to select preferred currency with date on how far back to look for data. There's also a boolean modifier which basically forces the system to download currency history from the LbLt site and saves it into the database for future re-use. 

Calculator works in both ways. Can  write to get amount in **EUR** in selected currency and vice versa.

Hope you enjoy it as much as I enjoyed making it! Looking forward for feedback as well on what could be improved!
# 🗿