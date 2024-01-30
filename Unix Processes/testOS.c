#include <stdio.h>

#include <sys/types.h>

#include <unistd.h>

int main (int args, char *argv[])

{

pid_t fork_return;

pid_t pid;

pid=getpid();

fork_return = fork();



if (fork_return==-1) {
// When fork() returns -1, an error has happened.
 printf("\nError creating process " );

 return 0;

 }
if (fork_return==0) {

// When fork() returns 0, we are in the child process.
//printf("\n\nThe values are Child ID = %d, Parent ID=%d\n", getpid(), getppid());

execl("/bin/cat", "cat", "-b", "-v", "-t", argv[1], NULL);

printf("\n\nThe values are Child ID = %d, Parent ID=%d\n", getpid(), getppid());


}

else {

// When fork() returns a positive number, we are in the parent process
//
// and the return value is the PID of the newly created child process.
wait(NULL);

printf("\nChild Completes " );

printf("\nIn the Parent Process\n");

printf("Child Id = %d, Parent ID = %d\n", getpid(), getppid());

}

return 0;

}


