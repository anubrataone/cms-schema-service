# cms-schema-service

## Synopsis

This service provides schema creation - management - validation service

### Standard Request Header

| Field | Data Type | Description |
| :---:   |   :---:   |   :---:              |
| X-Auth-Token    | STRING | This is base64 encoded username:password format. This is used to authenticate the user invoking API (For now: once we have Auth Integrated this should be the OVAT token)	|


### Standard HTTP Response Codes

| HTTP Code | Description |
| :---:   |   :---:   |
| 200 OK    | Successful response. |
| 401 Unauthorized    | Whenever a API requires a invalid user token/OVAT OR if it is not provided in the request. |
| 403 Forbidden    | Whenever a API requires a valid user token/OVAT but the provided session Token/OVAT expired or the OVAT signature mismatch or user doesn't have acess for that API |
| 404 Not Found    | If the requested ID or Resource not found in backend |
| 500 Server Error    | If the validation failed |




### Standard Response Body & Fields

| Field | Data Type | Description |
| :---:   |   :---:   |   :---:              |
| header    | Object | Standard header object.	|
| header -> code    | Integer | Response Code: 0 –> Success,  -1 –> Failure	|
| header -> message    | String | Response Message. Optional for failure response. For success response this should have the success message displayed to the end user.	|
| header -> system_time    | Timestamp | Unix Epoch Time in Milliseconds	|
| Errors    | Object | Standard errors object	|
| errors -> code| String | Error Code	|
| errors -> description| String | Error Description	|
| Data| Object | Container for the response object.	|

### Standard Success Response for single value response - GET CALL

```
{
    "header": {
        "source": "<Source Micro-service Name>",
        "code": "0",
        "message": "Success",
        "system_time": 1558041284123
    },
    "data": {
        <RESPONSEOBJECT>
    }
}
```
### Standard Success Response for multi-value response - GET CALL

```
{
    "header": {
        "source": "Micro-service Name",
        "code": "0",
        "message": "Success",
        "system_time": 1558041284123
        "start": 0,
        "rows": 10,
        "count": 100
    },
    "data": [
        <RESPONSEOBJECTLIST>
    ]
}
```

 ### Standard Success Response (Plain acknowledgement) - for insertion / update /deletion ACK

```
{
    "header": {
        "source": "<Source Micro-service Name>",
        "code": "0",
        "message": "Success",
        "system_time": 1558041284123
    }
}
```
 ### Standard Failure Response (Plain acknowledgement)

 ```
 {
     "header": {
         "source": "<Source Micro-service Name>",
         "code": "-1",
         "message": "Failure Message",
         "system_time": 1558041284123
         "errors": [
             {
                 "code": "Error Code 1",
                 "description": "Error Description 1"
             },
             {
                 "code": "Error Code 2",
                 "description": "Error Description"
             }
         ]
     },
     --  this data part is optional
     "data": [
         <RESPONSEOBJECTLIST>
     ]
 }
 ```

## API Listing

#####  POST CALLS

/schema/

```
- Method: POST
- Request line:
    
    - Body
        Content-type: application/json
        Encoded as a JSON Object for the fields to be updated.

    Blueprint name and blueprint version is the identifier.
    Code creates a new UUID for the inserted object.    

    

```

| Field | Data Type | Description |
| :---:   |   :---:   |   :---:              |
| _id    | String | ObjectId	|
| bpName    | String | {VODTitle / Series / Season / Provider/ Channel / Program / Schedule}	|
| bpVersion    | Integer | 	|
| urn    | String | Concatenate (bpName,bpVersion)	|
| status    | String | ACTIVATE/DEACTIVATE	|

Rest JSON data are free flow 


##### PUT CALLS

/schema/{id}


```
- Method: PUT
- Request line:
    - Path Param 
        entityType : name of collection
        id: Object Id
    - Body
        Content-type: application/json
        Encoded as a JSON Object for the fields to be updated.

    Blueprint name and blueprint version is the identifier.



```


Example:



This can be full document replace or just set particular fields.

| Field | Data Type | Description |
| :---:   |   :---:   |   :---:              |
| _id    | String | ObjectId	|
| bpName    | String | {VODTitle / Series / Season / Provider/ Channel / Program / Schedule}	|
| bpVersion    | Integer | 	|

Rest JSON data are free flow for whole BP or just set of fields.




##### GET CALLS

/schema/{bpName}/{bpVersion}


/schema/{bpName}

```
- Method: GET
- Request line:
    - Path params
      - bpName: blueprint name 
      - bpVersion: blueprint version
    
```



##### DELETE CALLS

/schema/{bpName}/{bpVersion}

```
- Method: POST
- Request line:

    - Body:
    Content-type: application/json
    type: DELETE
    
```

#####  POST CALLS


/schema/validate

```
- Method: POST
- Request line:

    - Body:
    Content-type: application/json
    - bpName: blueprint name 
    - bpVersion: blueprint version
    - data : resource json body
    
    
```


