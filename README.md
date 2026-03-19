I only did this becuase I didn't like to see the button

Current set up for bot with turret on

The code for without change the shootCommand to:

 public void initialize() {
   // turretShootSubSystem.prep(0.8);
   turretShootSubSystem.startTunnel(0.8);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

   // if (turretShootSubSystem.getSpeed() > 0.75) {
   //   turretShootSubSystem.startTunnel(0.8);
   //   
   // }

  } 

  this jsut disables the prep phase and disables the turret
