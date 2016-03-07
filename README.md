# lambda-test-java

[![Build Status](https://travis-ci.org/berezovskyi/lambda-test-java.svg?branch=master)](https://travis-ci.org/berezovskyi/lambda-test-java) [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/berezovskyi/lambda-test-java/master/LICENSE)

All test functions are fed with the following input:

    {
        "operation": "create",
        "tableName": "evts-at-rest-python",
        "payload": {
            "Item": {
                "name": "FTW",
                "count": "1"
            }
        }
    }

