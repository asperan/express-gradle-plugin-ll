#!/bin/bash

# Test route 'projects'
TEST_ROUTE="projects"
echo "Testing GET /projects"
curl -X GET "localhost:3000/${TEST_ROUTE}"
echo
echo

echo "Testing POST /projects"
curl -X POST "localhost:3000/${TEST_ROUTE}"
echo
echo

TEST_ID="testId"
echo "Testing GET /projects"
curl -X GET "localhost:3000/${TEST_ROUTE}/${TEST_ID}"
echo
echo

echo "Testing PUT /projects"
curl -X PUT "localhost:3000/${TEST_ROUTE}/${TEST_ID}"
echo
echo

echo "Testing DELETE /projects"
curl -X DELETE "localhost:3000/${TEST_ROUTE}/${TEST_ID}"
echo
echo
