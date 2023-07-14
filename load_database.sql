CREATE TABLE accounts (
	accountID SERIAL PRIMARY KEY,
	userID TEXT NOT NULL,
	password TEXT NOT NULL,
    rank TEXT NOT NULL
);
CREATE TABLE sessions (
	accountID INT NOT NULL,
	token TEXT NOT NULL UNIQUE,
	createdAt TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
	expireAt TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW() + INTERVAL '30 minutes'
)