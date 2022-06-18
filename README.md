# performances-app

## Architecture

### tickets-client

As the clients app and the validator app are Android applications, they are hard to sctructure and mantain, so it is not as organized as the ticket-server.

### tickets-server

In this case, the hexagonal architeture has been applied in order to make a clean and structurate visualization.

## Folder structure

### tickets-client

The usual Android architecture:

```
.
├── MainActivity.kt
├── api
│   ├── PerformancesAPI.kt
│   ├── TicketsAPI.kt
│   └── UsersAPI.kt
├── config
│   └── config.kt
├── db
│   └── TicketsDBAdapter.kt
├── middleware
│   └── SecureStore.kt
├── models
│   ├── BuyPayloadType.kt
│   ├── PerformanceType.kt
│   ├── QRInfoType.kt
│   ├── RegisterPayload.kt
│   ├── SignatureType.kt
│   ├── TicketInternalType.kt
│   ├── TicketType.kt
│   └── UserType.kt
├── services
│   ├── qrSrv.kt
│   ├── ticketSrv.kt
│   └── userSrv.kt
└── ui
    ├── NavBar.kt
    ├── home
    │   ├── BuyTicketBottomSheetFragment.kt
    │   ├── HomeFragment.kt
    │   ├── HomeViewModel.kt
    │   ├── PerformancesListAdapter.kt
    │   └── SinglePerformance.kt
    ├── login
    │   └── ui
    │       ├── Load.kt
    │       ├── Login.kt
    │       └── Register.kt
    ├── settings
    │   ├── SettingsFragment.kt
    │   └── SettingsViewModel.kt
    ├── tickets
    │   ├── TicketsFragment.kt
    │   ├── TicketsViewModel.kt
    │   └── my_tickets
    │       ├── MyPastTicketsFragment.kt
    │       ├── MyTicketFragment.kt
    │       ├── TicketPagerAdapter.kt
    │       ├── TicketsBottomsheetFragment.kt
    │       └── TicketsPageTransformer.kt
    └── utils
        ├── Biometrics.kt
        └── functions.kt
```

### tickets-server

```
├── Dockerfile
├── Procfile
├── README.md
├── app/
│   └── src/
│       └── main/
│           ├── kotlin/
│           │   └── pt/
│           │       └── feup/
│           │           └── performances/
│           │               ├── Application.kt
│           │               └── Configuration.kt
│           └── resources/
│               ├── application.yaml
│               └── static/
│                   ├── index.html
│                   └── js/
│                       └── app.js
├── core/
│   └── src/
│       └── main/
│           └── kotlin/
│               └── pt/
│                   └── feup/
│                       └── performances/
│                           └── core/
│                               ├── Domain.kt
│                               ├── Exceptions.kt
│                               ├── Ports.kt
│                               └── usecases/
│                                   ├── PerformancesUseCase.kt
│                                   ├── TicketsUseCase.kt
│                                   └── UsersUseCase.kt
├── delivery/
│   └── src/
│       └── main/
│           └── kotlin/
│               └── pt/
│                   └── feup/
│                       └── performances/
│                           └── infrastructure/
│                               └── delivery/
│                                   ├── ExceptionHandlers.kt
│                                   ├── PerformancesController.kt
│                                   ├── PortsImpl.kt
│                                   ├── TicketsController.kt
│                                   ├── UsersController.kt
│                                   └── middleware/
│                                       ├── Config.kt
│                                       └── SecureStore.kt
├── gradlew*
├── gradlew.bat
├── repositories/.../infrastructure/repositories/
│                                   ├── Converters.kt
│                                   ├── Entities.kt
│                                   ├── PortsImpl.kt
│                                   └── Repositories.kt
├── settings.gradle.kts
└── system.properties
```

## Local deployment

With localhost, we can set up the local tickets-server and the tickets-images-server.

**Warning:** The local tickets-server and the local tickets-images-server will be running on docker containers, so the real mobile (which has the tickets-validator installed) will not be able to access them.

```
> docker-compose build
> docker-compose up
```

## Production deployment

Both tickets-server and tickets-images-server are deployed on Heroku, so the mobile apps can access them from the internet.

To deploy the tickets-server we have created a [system.properties](tickets-server/system.properties) file in its root directory to say that it must be run in Java 11. Also, a [Procfile](tickets-server/Procfile) file has been created to indicate the correct entrypoint:

> web: java -jar app/build/libs/app.jar --server.port=$PORT

As for the tickets-images-server, it is simply deployed in Heroku without further configuration, by simply running:

```
> heroku login
> keroku create
> git init
> git add .
> git commit -m "..."
> git push heroku master
> heroku open
```

## Testing

No automated tests yet done.

Acceptance tests to be done manually:

### Register a new user

- [ ] Try to register a new user with invalid inputs
- [ ] Register a new user with valid inputs

### Log a user locally

- [ ] Try to log in with invalid credentials
- [ ] Log in with valid PIN code
- [ ] Log in with fingerprint

### Load list of performances

- [ ] Load an empty list
- [ ] Load a list with some performances
- [ ] Load a list with more than 10 performances to see lazy loading
- [ ] See that one performance is sold out and try to tap it

### Buy a ticket

- [ ] Buy a ticket
- [ ] Reload list of performances and see that the seat number has been updated
- [ ] See that the ticket have been bought and saved correctly
- [ ] Try to buy more than 5 tickets of the same performance
- [ ] See that the ticket have been saved correctly

### Validate a ticket

- [ ] Try to validate a ticket with invalid date
- [ ] Validate a ticket with valid date
- [ ] Try to revalidate a ticket again

### Delete a ticket

- [ ] Try to delete a ticket with a date before the current date
- [ ] Delete a ticket with a date after the current date
- [ ] See that the ticket has correctly been deleted

## Bugs

- When deleting a stored ticket of the SQLite database the ticket arraylist in the TicketsViewModel has weird behaviours when update state.
- Some inputs are not completely bounded
- When a user buys a ticket, the ticket information should be sent back to the user encrypted by the user public key. The problem was that the signed bytes must be at most the same size of the key. The solution is send the ticket information in batches (not implemented)
