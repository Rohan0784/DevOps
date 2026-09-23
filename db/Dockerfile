# Use the latest MySQL image
FROM mysql:8.4

# Set the working directory
WORKDIR /tmp

# Copy all the files to the working directory of the container
COPY test_db/*.sql /tmp/
COPY test_db/*.dump /tmp/

# Scripts and SQL files in this folder run during first database initialization.
COPY test_db/employees.sql /docker-entrypoint-initdb.d/

# Set the root password
ENV MYSQL_ROOT_PASSWORD=example
